package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.request.callback.*;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.MixStream;
import cn.bixin.sona.server.room.domain.db.MixStreamReplay;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.Stream;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.domain.stream.UserSginInputParam;
import cn.bixin.sona.server.room.mapper.MixStreamMapper;
import cn.bixin.sona.server.room.mapper.MixStreamReplayMapper;
import cn.bixin.sona.server.room.mapper.StreamMapper;
import cn.bixin.sona.server.room.service.RoomService;
import cn.bixin.sona.server.room.service.StreamService;
import cn.bixin.sona.server.room.utils.TLSSigAPIv2;
import cn.bixin.sona.server.room.utils.ZegoUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StreamServiceImpl implements StreamService {
    private static final Logger log = LoggerFactory.getLogger(StreamServiceImpl.class);

    @Value("${tencent.prefix.url}")
    private String tencentPrefixUrl;
    @Value("${zego.prefix.url}")
    private String zegoMixedPrefixUrl;
    @Value("${zego.appsign}")
    private String zegoAppSign;
    @Value("${zego.app.id}")
    private int zegoAppId;
    @Value("${tencent.secretkey}")
    private String tencentSecretKey;
    @Value("${tencent.appid}")
    private int tencentAppId;

    @Resource
    private IdGenerator idGenerator;
    @Resource
    private StreamMapper streamMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private RoomService roomService;
    @Resource
    private MixStreamMapper mixStreamMapper;
    @Resource
    private MixStreamReplayMapper mixStreamReplayMapper;


    private static final String KEY_STREAM_INFO = "si:s";

    public static String getStreamInfo(String streamId) {
        return Joiner.on(":").join(KEY_STREAM_INFO, streamId);
    }


    private static final String CREATE_STREAM_KEY = "cr:s:k";

    private String getCreateStreamKey(String streamId) {
        return Joiner.on(":").join(CREATE_STREAM_KEY, streamId);
    }

    @Override
    public String initStream(long roomId, long uid, ProductConfig config) {
        String productCode = config.getProductCode();

        String streamId = doGenerateStreamId(roomId, uid, config);
        streamMapper.initStream(streamId, roomId, uid, productCode, StreamSupplierEnum.valueOf(config.getStreamSupplier()).getCode());

        Stream streamInfo = new Stream();
        streamInfo.setUid(uid);
        streamInfo.setRoomId(roomId);
        streamInfo.setProductCode(productCode);
        streamInfo.setStreamId(streamId);

        redisTemplate.opsForValue().set(getStreamInfo(streamId), JSON.toJSONString(streamInfo), 1, TimeUnit.MINUTES);

        return streamId;
    }

    private String doGenerateStreamId(long roomId, long uid, ProductConfig config) {
        String connector = StreamSupplierEnum.getDescBySupplier(config.getStreamSupplier());
        if (StringUtils.isBlank(connector)) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }

        String streamId = "S" +
                config.getShortCode() + connector +
                roomId + connector +
                uid + connector +
                idGenerator.strId();
        return streamId.length() <= 64 ? streamId : streamId.substring(0, 64);
    }

    @Override
    public Stream getUserLivingStream(long roomId, long uid) {
        List<Stream> streams = streamMapper.selectLivingStreamByRoomIdAndUids(roomId, Lists.newArrayList(uid));
        if (CollectionUtils.isEmpty(streams)) {
            return null;
        }
        return streams.get(0);
    }

    @Override
    public Stream getStreamByStreamId(String streamId) {
        log.info("getStreamByStreamId streamId:{}", streamId);
        String streamStr = redisTemplate.opsForValue().get(getStreamInfo(streamId));
        if (StringUtils.isBlank(streamStr)) {
            Stream stream = streamMapper.selectByStreamId(streamId);
            redisTemplate.opsForValue().set(getStreamInfo(streamId), JSON.toJSONString(stream), 1, TimeUnit.MINUTES);
            return stream;
        }

        try {
            return JSON.parseObject(streamStr, Stream.class);
        } catch (Exception e) {
            log.error("parse stream obj error. e:{}", e);
            return streamMapper.selectByStreamId(streamId);
        }
    }

    @Override
    public Map<Long, Stream> batchGetUserLivingStream(long roomId, List<Long> uids) {
        List<Stream> streams = streamMapper.selectLivingStreamByRoomIdAndUids(roomId, uids);
        return streams.stream().collect(Collectors.toMap(Stream::getUid, Function.identity(), (v1, v2) -> v2));
    }

    @Override
    public List<Stream> getRoomLivingStream(long roomId) {
        return streamMapper.getRoomLivingSteamList(roomId);
    }

    @Override
    public boolean addStream(StreamContext streamContext) {
        String streamId = streamContext.getStreamId();
        Stream stream = streamMapper.selectByStreamId(streamId);
        if (stream != null) {
            log.info("addStream streamId is exist, {}", streamId);
            return false;
        }

        Stream record = new Stream();
        record.setStreamId(streamId);
        record.setRoomId(streamContext.getRoomId());
        record.setStatus(1);
        record.setUid(streamContext.getUid());
        record.setSource(streamContext.getStreamSupplier().getCode());
        record.setProductCode(streamContext.getProductCode());
        streamMapper.addStream(record);
        return true;
    }

    @Override
    public boolean closeStream(String streamId) {
        Stream stream = streamMapper.selectByStreamId(streamId);
        if (stream == null || stream.getStatus() == 0) {
            return false;
        }
        streamMapper.closeStream(streamId, 0, "正常关闭");
        return true;
    }

    @Override
    public String getPlayUrl(long roomId, String supplier) {
        if (StreamSupplierEnum.TENCENT.name().equals(supplier)) {
            return tencentPrefixUrl + roomId + ".flv";
        } else if (StreamSupplierEnum.ZEGO.name().equals(supplier)) {
            return zegoMixedPrefixUrl + roomId;
        }
        return null;
    }

    @Override
    public AppInfoDTO genUserSig(UserSginInputParam userSginInputParam) {
        String streamSupplier = userSginInputParam.getStreamSupplier();
        boolean needReplay = userSginInputParam.isNeedReplay();
        long uid = userSginInputParam.getUid();

        log.info("genUserSig supplier:{}, needReplay:{}, uid:{}", streamSupplier, needReplay, uid);
        if (StreamSupplierEnum.ZEGO.name().equals(streamSupplier)) {
            return getZegoUserSig(uid);
        } else if (StreamSupplierEnum.TENCENT.name().equals(streamSupplier)) {
            return getTencentUserSig(uid);
        }

        return null;
    }

    private AppInfoDTO getZegoUserSig(long uid) {
        AppInfoDTO appInfoDTO = new AppInfoDTO();
        appInfoDTO.setAppId(zegoAppId);
        appInfoDTO.setAppID(String.valueOf(zegoAppId));
        appInfoDTO.setAppSign(zegoAppSign);
        appInfoDTO.setToken(ZegoUtils.getZeGouToken(String.valueOf(zegoAppId), zegoAppSign, String.valueOf(uid)));
        return appInfoDTO;
    }

    private AppInfoDTO getTencentUserSig(long uid) {
        AppInfoDTO appInfoDTO = new AppInfoDTO();
        appInfoDTO.setAppId(tencentAppId);
        appInfoDTO.setAppID(String.valueOf(tencentAppId));
        long expireTime = 604800;
        TLSSigAPIv2 api = new TLSSigAPIv2(tencentAppId, tencentSecretKey);
        appInfoDTO.setAppSign(api.genSig(String.valueOf(uid), expireTime));
        return appInfoDTO;
    }

    @Override
    public boolean createStreamCallback(CreateStreamCallback callback) {
        log.info("createStreamCallback. {}", JSON.toJSONString(callback));
        String streamId = callback.getStreamId();
        String picUrl = org.springframework.util.CollectionUtils.isEmpty(callback.getPicUrls()) ? ""
                : callback.getPicUrls().get(0);
        Stream stream = streamMapper.selectByStreamId(streamId);
        if (stream != null && (stream.getStatus() == 1 || stream.getStatus() == -1)) {
            log.info("createStreamCallback streamId is exist, {}", JSON.toJSONString(stream));
            //对于已经存在数据不做更新
            String rtmpUrls = StringUtils.isNotBlank(stream.getRtmpUrl()) ? null : JSON.toJSONString(
                    callback.getRtmpUrls());
            String hlsUrls = StringUtils.isNotBlank(stream.getHlsUrl()) ? null : JSON.toJSONString(
                    callback.getHlsUrls());
            String hdlUrls = StringUtils.isNotBlank(stream.getHdlUrl()) ? null : JSON.toJSONString(
                    callback.getHdlUrls());
            picUrl = StringUtils.isNotBlank(stream.getPicUrl()) ? null : picUrl;
            streamMapper.updateCreateStream(streamId, rtmpUrls, hlsUrls, hdlUrls, picUrl);
        } else if (stream != null && stream.getStatus() == 0) {
            streamMapper.openStream(streamId);
        } else {
            RoomDTO roomDTO = roomService.getRoomByRoomId(callback.getRoomId());
            Stream record = new Stream();
            record.setStreamId(streamId);
            record.setRoomId(callback.getRoomId());
            record.setStatus(1);
            record.setUid(callback.getUid());
            record.setSource(callback.getSource());
            record.setRtmpUrl(JSON.toJSONString(callback.getRtmpUrls()));
            record.setHlsUrl(JSON.toJSONString(callback.getHlsUrls()));
            record.setHdlUrl(JSON.toJSONString(callback.getHdlUrls()));
            record.setPicUrl(picUrl);
            record.setCloseType(-1);
            record.setProductCode(roomDTO.getProductCode());
            log.info("createStreamCallback insert stream {}", JSON.toJSONString(record));
            streamMapper.insert(record);
        }
        return true;
    }

    @Override
    public boolean closeStreamCallback(CloseStreamCallback callback) {
        Stream stream = streamMapper.selectByStreamId(callback.getStreamId());
        if (stream == null || stream.getStatus() == 0) {
            return false;
        }
        streamMapper.closeStream(callback.getStreamId(), callback.getCloseType(), "正常关闭");
        return true;
    }

    @Override
    public void replayStreamCallback(CreateReplayCallback callback) {
        streamMapper.handleReplay(callback.getStreamId(), callback.getReplayUrl(), callback.getBeginTime(), callback.getEndTime());
    }

    @Override
    public boolean handleMixReplay(CreateReplayCallback callback) {
        if (callback == null || StringUtils.isEmpty(callback.getStreamId())) {
            return false;
        }

        MixStream mixStream = mixStreamMapper.findLatestByStreamId(callback.getStreamId(), callback.getSource());
        if (mixStream == null) {
            log.info("handleMixReplay failed, not found mixStream record, streamId:{}", callback.getStreamId());
            return false;
        }

        MixStreamReplay replay = MixStreamReplay.converter(mixStream, callback.getReplayUrl(), callback.getBeginTime(), callback.getEndTime());
        int num = mixStreamReplayMapper.insert(replay);
        log.info("handleMixReplay, insert mixReplayRecord result:{}, streamId:{}", num, callback.getStreamId());
        return true;
    }

    @Override
    public void addMixStream(MixStreamStartCallback callback, long roomId) {
        MixStream mixStream = new MixStream();
        mixStream.setRoomId(roomId);
        mixStream.setBizRoomId(callback.getBizRoomId());
        mixStream.setStreamId(callback.getStreamId());
        mixStream.setSource(callback.getSource());
        mixStream.setRtmpUrl(JSON.toJSONString(callback.getRtmpUrls()));
        mixStream.setHlsUrl(JSON.toJSONString(callback.getHlsUrls()));
        mixStream.setHdlUrl(JSON.toJSONString(callback.getHdlUrls()));
        mixStream.setInputStream(JSON.toJSONString(callback.getInputStreamList()));
        mixStream.setStatus(1);
        mixStream.setBeginTime(callback.getCreateTime());
        mixStream.setEndTime(callback.getCreateTime());
        mixStreamMapper.insertSelective(mixStream);
    }

    @Override
    public void stopMixStream(MixStreamEndCallback callback) {
        mixStreamMapper.stopMixStream(callback.getStreamId(), callback.getSource(), callback.getCreateTime());
    }
}
