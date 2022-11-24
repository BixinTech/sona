package cn.bixin.sona.server.room.strategy.stream;

import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.server.room.domain.enums.ProductEnum;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.generator.StreamIdGenerator;
import cn.bixin.sona.server.room.service.ProductConfigService;
import cn.bixin.sona.server.room.service.StreamService;
import cn.bixin.sona.server.room.service.TencentService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component("tencentStrategy")
public class TencentStrategy extends DefaultStrategy {

    private static final Logger log = LoggerFactory.getLogger(TencentStrategy.class);

    @Resource
    private StreamService streamService;
    @Resource
    private StreamIdGenerator streamIdGenerator;
    @Resource
    private ProductConfigService productConfigService;

    @Resource
    private TencentService tencentService;

    @Value("${tencent.prefix.url}")
    private String tencentPrefixUrl;

    @Value("${tencent.rtmp.url}")
    private String tencentRtmpUrl;

    @Override
    public void handleCreate(CreateStreamCallback callback) {
        String streamId = callback.getStreamId();
        ProductEnum platform = streamIdGenerator.getPlatform(streamId);
        if (platform == null) {
            return;
        }
        try {
            StreamContext streamContext = StreamContext.convert(streamId);
            if (streamContext == null) {
                log.error("tencent handleCreate streamContext is null, callback={}", JSON.toJSONString(callback));
                return;
            }
            String rtmpUrl = tencentRtmpUrl + streamId;
            String hlsUrl = tencentPrefixUrl + streamId + ".m3u8";
            String hdlUrl = tencentPrefixUrl + streamId + ".flv";
            String picUrl = "/pic";
            callback.setRoomId(streamContext.getRoomId());
            callback.setStreamId(streamId);
            callback.setSource(2);
            callback.setRtmpUrls(Lists.newArrayList(rtmpUrl));
            callback.setHlsUrls(Lists.newArrayList(hlsUrl));
            callback.setHdlUrls(Lists.newArrayList(hdlUrl));
            callback.setPicUrls(Lists.newArrayList(picUrl));
            callback.setCreateTime(new Date());
            callback.setUid(streamContext.getUid());
            boolean result = streamService.createStreamCallback(callback);
            log.info("tencent handleCreate result:{}, streamId={}", result, streamId);
            long roomId = streamContext.getRoomId();
            if (isMixed(productConfigService.getRoomConfig(roomId), platform)) {
                tencentService.mix(roomId);
            }
        } catch (Exception e) {
            log.error("tencent handleCreate error. streamId:{}", streamId, e);
        }
    }

    @Override
    public void handleClose(CloseStreamCallback callback) {
        String streamId = callback.getStreamId();
        ProductEnum platform = streamIdGenerator.getPlatform(streamId);
        if (platform == null) {
            return;
        }
        try {
            StreamContext streamContext = StreamContext.convert(streamId);
            if (streamContext == null) {
                log.error("tencent handleClose streamContext is null, callback={}", JSON.toJSONString(callback));
                return;
            }
            callback.setRoomId(String.valueOf(streamContext.getRoomId()));
            streamService.closeStreamCallback(callback);
        } catch (Exception e) {
            log.error("tencent handleClose error. streamId:{}", streamId, e);
        }
    }

}
