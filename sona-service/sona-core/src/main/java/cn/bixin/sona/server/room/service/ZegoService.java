package cn.bixin.sona.server.room.service;

import cn.bixin.sona.common.trace.TraceHelper;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.enums.RoomMixedEnum;
import cn.bixin.sona.server.room.client.MixStreamClient;
import cn.bixin.sona.server.room.config.RoomAsyncConfig;
import cn.bixin.sona.server.room.domain.convert.MixConfigConverter;
import cn.bixin.sona.server.room.domain.db.MixConfig;
import cn.bixin.sona.server.room.domain.db.Stream;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.domain.request.ZegoMixRequest;
import cn.bixin.sona.server.room.domain.request.ZegoStopMixRequest;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.manager.AudioSwitchManager;
import cn.bixin.sona.server.room.utils.ZegoUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author qinwei
 */
@Service
public class ZegoService {

    private static final Logger log = LoggerFactory.getLogger(ZegoService.class);

    @Resource
    private StreamService streamService;
    @Resource
    private IdGenerator idGenerator;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private MixStreamClient mixStreamClient;

    @Autowired
    private MixConfigService mixConfigService;

    @Resource
    private AudioSwitchManager audioSwitchManager;

    @Value("${zego.app.id}")
    private int zegoAppId;

    @Value("${zego.app.secret}")
    private String zegoSecret;

    @Value("${zego.start.mix.url}")
    private String startMixStreamUrl;

    @Value("${zego.stop.mix.url}")
    private String stopMixStreamUrl;

    @Value("${zego.token.url}")
    private String zegoTokenUrl;

    @Value("${zego.server.secret}")
    private String zegoServerSecret;

    @Value("${zego.mix.retry.count}")
    private int retry;

    private static final String KEY_ZEGO_ACCESS_TOKEN = "zego:access:token";

    private static String getKeyZegoAccessToken(int zegoAppId) {
        return Joiner.on(":").join(KEY_ZEGO_ACCESS_TOKEN, zegoAppId);
    }

    @Async("mixStreamExecutor")
    public void mix(long roomId) {
        TraceHelper.init();
        log.info("start zego mix , roomId:{}", roomId);
        Transaction transaction = Cat.newTransaction("ZegoMixStream", String.valueOf(roomId));
        try {
            List<Stream> streamList = getValidStreamIds(roomId);
            if (CollectionUtils.isEmpty(streamList)) {
                log.info("mix streamList is null. roomId:{}", roomId);
                return;
            }
            String result = doMix(streamList, roomId);

            int index = 1;
            while (!doMixSuccess(result) && doMixNonExistStream(result) && index < retry) {
                List<String> notExistStreamList = getNotExistSteamList(result);
                if (!CollectionUtils.isEmpty(notExistStreamList)) {
                    notExistStreamList.forEach(s -> streamService.closeStream(s));
                }

                streamList = getValidStreamIds(roomId);
                if (CollectionUtils.isEmpty(streamList)) {
                    result = "{\"code\":0,\"data\":{}}";
                    break;
                }

                result = doMix(streamList, roomId);
                index++;
            }

            if (mixTimeoutOrOverclock(result)) {
                Cat.logEvent("ZegoMixTimeoutOrOverclock", String.valueOf(roomId));
                mixStream(roomId);
            } else if (!doMixSuccess(result)) {
                log.warn("zego mix stream failed, swith to single, roomId:{}, result:{}", roomId, result);
                audioSwitchManager.roomSwitch(Lists.newArrayList(roomId), RoomMixedEnum.MIXED_ZEGO_SINGLE);
            }
        } catch (Exception e) {
            log.error("zego mix stream failed, roomId:{}", roomId, e);
            transaction.setStatus(e);
        } finally {
            transaction.complete();
            TraceHelper.reset();
        }
    }

    private String doMix(List<Stream> mixStreams, long roomId) {
        log.info("doMix roomId:{}, zegoAppId:{}", roomId, zegoAppId);
        //get zego mix request
        ZegoMixRequest mixRequest = getZegoMixRequest(String.valueOf(roomId), zegoAppId);

        List<ZegoMixRequest.MixInput> mixInputList = Lists.newArrayList();
        //获取mix config 配置
        MixConfig mixConfig = mixConfigService.getMixConfigOrDefaultAudioByRoomId(roomId);
        log.info("get mix config, mixConfig: {}", mixConfig);

        mixStreams.forEach(stream -> {
            ZegoMixRequest.MixInput mixInput = getMixInput(stream.getStreamId(), mixConfig);
            mixInput.setSound_level_id((int) stream.getUid());
            mixInputList.add(mixInput);
        });
        mixRequest.setMixInput(mixInputList);

        List<ZegoMixRequest.MixOutput> mixOutputList = Lists.newArrayList();
        ZegoMixRequest.MixOutput mixOutput = getMixOutput(String.valueOf(roomId), mixConfig);
        mixOutputList.add(mixOutput);
        mixRequest.setMixOutput(mixOutputList);

        String accessToken = getAccessToken();
        String result = mixStreamClient.sendPost(startMixStreamUrl + accessToken, JSON.toJSONString(mixRequest));
        log.info("doMix result:{}, roomId:{}", result, roomId);
        return result;
    }

    /**
     * get zego mix request
     */
    private ZegoMixRequest getZegoMixRequest(String masterStream, int zegoAppId) {
        ZegoMixRequest request = new ZegoMixRequest();
        long timestamp = System.currentTimeMillis();
//        request.setSignature(ZegoUtils.md5(zegoAppId + timestamp + zegoSecret));
        request.setTimestamp(timestamp);
        request.setId_name(masterStream);
        request.setAppid(zegoAppId);
        request.setSeq(Integer.parseInt(idGenerator.strId().substring(0, 9)));
        request.setVersion(1);
        request.setWith_sound_level(1);
        return request;
    }

    /**
     * get mix input
     */
    private static ZegoMixRequest.MixInput getMixInput(String stream, MixConfig mixConfig) {
        ZegoMixRequest.MixInput mixInput = new ZegoMixRequest.MixInput();
        mixInput.setStream_id(stream);
        ZegoMixRequest.RectInfo rectInfo = new ZegoMixRequest.RectInfo();
        rectInfo.setTop(mixConfig.getTops());
        rectInfo.setLeft(mixConfig.getLefts());
        if (StreamContext.convertStringUid(stream).equals(mixConfig.getUid()) &&
                MixConfigConverter.inMVStatus(mixConfig)) {
            //混入MV用户, 单独设置配置
            rectInfo.setBottom(mixConfig.getBottom());
            rectInfo.setRight(mixConfig.getRights());
        } else {
            //其他用户默认设置音频参数
            rectInfo.setBottom(16);
            rectInfo.setRight(16);
        }
        mixInput.setRect(rectInfo);
        return mixInput;
    }

    /**
     * get mix output
     */
    private static ZegoMixRequest.MixOutput getMixOutput(String masterStream, MixConfig mixConfig) {
        ZegoMixRequest.MixOutput mixOutput = new ZegoMixRequest.MixOutput();
        mixOutput.setStream_id(masterStream);
        mixOutput.setBitrate(mixConfig.getBitrate());
        mixOutput.setFps(mixConfig.getFps());
        mixOutput.setHeight(mixConfig.getHeight());
        mixOutput.setWidth(mixConfig.getWidth());
        mixOutput.setAudio_enc_id(1);
        mixOutput.setAudio_bitrate(128000);
        mixOutput.setAudio_channel_cnt(2);
        return mixOutput;
    }

    private static boolean doMixSuccess(String result) {
        if (!StringUtils.hasText(result)) {
            return false;
        }
        JSONObject resultObj = JSON.parseObject(result);
        int code = resultObj.getIntValue("code");
        return code == 0;
    }

    private static boolean mixTimeoutOrOverclock(String result) {
        if (!StringUtils.hasText(result)) {
            return false;
        }
        JSONObject resultObj = JSON.parseObject(result);
        int code = resultObj.getIntValue("code");
        return code == -888 || code == 5 || code == -889;
    }

    private static boolean doMixNonExistStream(String result) {
        if (!StringUtils.hasText(result)) {
            return false;
        }
        JSONObject resultObj = JSON.parseObject(result);
        int code = resultObj.getIntValue("code");
        return code == 150;
    }

    private static List<String> getNotExistSteamList(String result) {
        JSONObject resultObj = JSON.parseObject(result);
        String message = resultObj.getString("data");
        JSONObject listObj = JSON.parseObject(message);
        List<Object> nonExistList = listObj.getJSONArray("non_exist_streams");
        if (CollectionUtils.isEmpty(nonExistList)) {
            return Collections.emptyList();
        }
        return nonExistList.stream().map(Object::toString).collect(Collectors.toList());
    }

    private List<Stream> getValidStreamIds(long roomId) {
        return streamService.getRoomLivingStream(roomId)
                .stream()
                .filter(stream -> StreamSupplierEnum.ZEGO.getCode() == stream.getSource())
                .collect(Collectors.toList());
    }

    private String getAccessToken() {
        String accessToken = stringRedisTemplate.opsForValue().get(getKeyZegoAccessToken(zegoAppId));
        log.info("getAccessToken, accessToken:{}, zegoAppId:{}", accessToken, zegoAppId);
        if (StringUtils.hasText(accessToken)) {
            return accessToken;
        }
        accessToken = getToken(zegoAppId, zegoServerSecret, zegoTokenUrl);
        if (accessToken == null) {
            return null;
        }
        stringRedisTemplate.opsForValue().set(getKeyZegoAccessToken(zegoAppId), accessToken, 30, TimeUnit.MINUTES);
        return accessToken;
    }

    public String getToken(int appid, String serverSecret, String url) {
        String result = mixStreamClient.sendGet(url, "appid=" + appid + "&secret=" + serverSecret);
        log.info("getToken, result:{}", result);
        if (result == null) {
            return null;
        }
        String token = JSON.parseObject(result).getJSONObject("data").getString("access_token");
        log.info("getToken, access_token:{}", token);
        return token;
    }

    private void mixStream(long roomId) {
        RoomAsyncConfig.MIX_STREAM_TIMER.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                mix(roomId);
            }
        }, 1500, TimeUnit.MILLISECONDS);
    }

    public String stopMix(String roomId) {
        ZegoStopMixRequest mixRequest = new ZegoStopMixRequest();
        long timestamp = System.currentTimeMillis();
        mixRequest.setAppid(zegoAppId);
        mixRequest.setTimestamp(timestamp);
        mixRequest.setSignature(ZegoUtils.md5(zegoAppId + timestamp + zegoSecret));
        mixRequest.setId_name(roomId);
        mixRequest.setLive_channel(roomId);
        mixRequest.setStream_id(roomId);
        mixRequest.setSeq(Integer.parseInt(idGenerator.strId().substring(0, 9)));
        return stopMix(mixRequest);
    }

    public String stopMix(ZegoStopMixRequest request) {
        String accessToken = getAccessToken();
        String result = mixStreamClient.sendPost(stopMixStreamUrl + accessToken, JSON.toJSONString(request));
        log.info("zego stopMix result:{}", result);
        return result;
    }

}
