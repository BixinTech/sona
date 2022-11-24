package cn.bixin.sona.server.room.strategy.stream;

import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.Stream;
import cn.bixin.sona.server.room.domain.enums.ProductEnum;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.generator.StreamIdGenerator;
import cn.bixin.sona.server.room.service.ProductConfigService;
import cn.bixin.sona.server.room.service.RoomService;
import cn.bixin.sona.server.room.service.StreamService;
import cn.bixin.sona.server.room.service.ZegoService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component("zegoStrategy")
public class ZegoStrategy extends DefaultStrategy {

    private static final Logger log = LoggerFactory.getLogger(ZegoStrategy.class);

    @Resource
    private ZegoService zegoService;

    @Resource
    private StreamService streamService;
    @Resource
    private StreamIdGenerator streamIdGenerator;
    @Resource
    private ProductConfigService productConfigService;
    @Resource
    private RoomService roomService;

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
                log.error("zego handleCreate streamContext is null, callback={}", JSON.toJSONString(callback));
                return;
            }
            callback.setUid(streamContext.getUid());
            boolean result = streamService.createStreamCallback(callback);
            log.info("zego handleCreate result:{}, streamId={}", result, streamId);
            long roomId = streamContext.getRoomId();
            if (isMixed(productConfigService.getRoomConfig(roomId), platform)) {
                zegoService.mix(roomId);
            }
        } catch (Exception e) {
            log.error("zego handleCreate error. stream error. streamId:{}", streamId, e);
        }
    }

    @Override
    public void handleClose(CloseStreamCallback callback) {
        String streamId = callback.getStreamId();
        ProductEnum platform = streamIdGenerator.getPlatform(streamId);
        if (platform == null) {
            log.warn("zego handleClose platform null. streamId:{}", streamId);
            return;
        }
        Stream streamInfo = streamService.getStreamByStreamId(streamId);
        if (streamInfo == null) {
            log.error("zego handleClose stream empty. streamId:{}", streamId);
            return;
        }
        boolean result = streamService.closeStreamCallback(callback);
        if (!result) {
            log.info("zego handlecloseStream, stream:{} already close", streamId);
        }
    }

    @Override
    public void handleAdd(StreamContext context) {
        if (context == null) {
            log.error("ZegoStrategy.handleAdd, context is null");
            throw new YppRunTimeException(ExceptionCode.EMPTY_PARAM);
        }

        String streamId = context.getStreamId();
        long roomId = context.getRoomId();

        RoomDTO roomDTO = roomService.getRoomByRoomId(roomId);
        if (roomDTO == null) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }

        boolean result = streamService.addStream(context);
        log.info("zegoStrategy handleAdd.addStream, stream:{}, result={}", streamId, result);
    }

    @Override
    public void handleClose(StreamContext context) {
        if (context == null) {
            throw new YppRunTimeException(ExceptionCode.EMPTY_PARAM);
        }
        String streamId = context.getStreamId();

        Stream streamInfo = streamService.getStreamByStreamId(streamId);
        if (streamInfo == null || streamInfo.getStatus() == 0) {
            return;
        }

        RoomDTO roomDTO = roomService.getRoomByRoomId(context.getRoomId());
        if (roomDTO == null) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }
        boolean result = streamService.closeStream(streamId);
        log.info("zegoStrategy handleClose.closeStream, stream:{}, result={}", streamId, result);
    }


}
