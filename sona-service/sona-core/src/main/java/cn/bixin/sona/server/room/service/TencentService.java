package cn.bixin.sona.server.room.service;

import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.server.room.domain.request.TencentStartMixRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dianping.cat.Cat;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.StartMCUMixTranscodeRequest;
import com.tencentcloudapi.trtc.v20190722.models.StartMCUMixTranscodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@Component
public class TencentService {

    private static final Logger log = LoggerFactory.getLogger(TencentService.class);

    @Resource
    private RoomService roomService;

    @Value("${tencent.secret.id}")
    private String secretId;

    @Value("${tencent.secret.key}")
    private String secretKey;

    @Value("${tencent.endpoint}")
    private String endpoint;

    @Value("${tencent.region}")
    private String region;

    @Value("${tencent.appid}")
    private int tencentAppId;

    @Async("mixStreamExecutor")
    public void mix(long roomId) {
        log.info("tencentStartMix roomId:{}", roomId);
        RoomDTO roomDTO = roomService.getRoomByRoomId(roomId);
        if (roomDTO == null) {
            return;
        }
        try {
            TrtcClient client = generateTrtcClient();
            TencentStartMixRequest request = new TencentStartMixRequest();
            request.setRoomId(roomId);
            request.setSdkAppId(tencentAppId);

            TencentStartMixRequest.OutputParams outputParams = new TencentStartMixRequest.OutputParams();
            outputParams.setStreamId(String.valueOf(roomId));
            outputParams.setPureAudioStream(0);
            TencentStartMixRequest.EncodeParams encodeParams = new TencentStartMixRequest.EncodeParams();
            encodeParams.setAudioSampleRate(48000);
            encodeParams.setAudioBitrate(128);
            encodeParams.setAudioChannels(2);
            encodeParams.setVideoWidth(16);
            encodeParams.setVideoHeight(16);
            encodeParams.setVideoBitrate(2);
            encodeParams.setVideoFramerate(15);
            encodeParams.setVideoGop(2);

            TencentStartMixRequest.LayoutParams layoutParams = new TencentStartMixRequest.LayoutParams();
            layoutParams.setTemplate(0);

            request.setEncodeParams(encodeParams);
            request.setLayoutParams(layoutParams);
            request.setOutputParams(outputParams);

            StartMCUMixTranscodeRequest req = StartMCUMixTranscodeRequest.fromJsonString(JSON.toJSONString(request), StartMCUMixTranscodeRequest.class);
            StartMCUMixTranscodeResponse resp = client.StartMCUMixTranscode(req);

            JSONObject resultObj = JSON.parseObject(StartMCUMixTranscodeRequest.toJsonString(resp));
            String requestId = resultObj.get("RequestId").toString();
            log.info("tencentMix success. roomId:{}, requestId:{}", roomId, requestId);
        } catch (TencentCloudSDKException e) {
            log.error("TencentCloudSDKException error!", e);
            Cat.logEvent("Tencent_MIX_ERROR", "Tencent_MIX" + roomId + ", requestId:" + e.getRequestId());
        } catch (Exception e) {
            log.error("tencentMix error!", e);
            Cat.logEvent("Tencent_MIX_ERROR", "Tencent_MIX" + roomId);
        }
    }

    private TrtcClient generateTrtcClient() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(endpoint);
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        return new TrtcClient(cred, region, clientProfile);
    }

}
