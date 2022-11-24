package cn.bixin.sona.server.room.strategy.stream;

import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.request.callback.*;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;
import cn.bixin.sona.server.room.domain.enums.ProductEnum;
import cn.bixin.sona.server.room.domain.enums.PullModeEnum;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.generator.StreamIdGenerator;
import cn.bixin.sona.server.room.service.*;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;


@Component("defaultStrategy")
public class DefaultStrategy implements StreamOperation {
    private static final Logger log = LoggerFactory.getLogger(DefaultStrategy.class);

    @Resource
    protected StreamService streamService;
    @Resource
    protected RoomService roomService;
    @Resource
    protected StreamIdGenerator streamIdGenerator;
    @Resource
    protected ProductConfigService productConfigService;

    @Override
    public void handleCreate(CreateStreamCallback callback) {

    }

    @Override
    public void handleClose(CloseStreamCallback callback) {

    }

    @Override
    public void handleReplay(CreateReplayCallback callback) {
        String streamId = callback.getStreamId();
        streamService.replayStreamCallback(callback);
        StreamSupplierEnum streamSupplier = StreamSupplierEnum.getByCode(callback.getSource());
        if (streamSupplier == null) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }
        StreamContext streamContext = StreamContext.convert(streamId);
        if (streamContext == null) {
            boolean handleMixReplay = streamService.handleMixReplay(callback);
            if (!handleMixReplay) {
                log.error("handleCreate streamContext is null, callback={}", JSON.toJSONString(callback));
            }
        }
    }

    @Override
    public void handleAdd(StreamContext context) {

    }

    @Override
    public void handleClose(StreamContext context) {

    }


    protected boolean isPullMixedMode(String productCode) {
        ProductConfig productConfig = productConfigService.getConfigInfoByCode(productCode);
        if (productConfig == null) {
            return false;
        }

        return productConfig.getPullMode().equals(PullModeEnum.MIXED.name());
    }

    protected List<String> getExistStreamList(String result, List<String> mixStreams) {
        List<String> nonExistSteamList = getNotExistSteamList(result);
        if (CollectionUtils.isEmpty(nonExistSteamList) || CollectionUtils.isEmpty(mixStreams)) {
            return mixStreams;
        }

        mixStreams.removeAll(nonExistSteamList);
        log.info("getExistStreamList stream:{}", mixStreams);
        return mixStreams;
    }

    protected List<String> getNotExistSteamList(String result) {
        return Collections.emptyList();
    }

    @Override
    public void handleMixStart(MixStreamStartCallback callback) {
        RoomDTO roomDTO = roomService.getRoomByRoomId(Long.valueOf(callback.getBizRoomId()));
        if (roomDTO == null) {
            log.error("handleMixStart error, roomDTO is null, callback={}", JSON.toJSONString(callback));
            return;
        }
        streamService.addMixStream(callback, roomDTO.getRoomId());
    }

    @Override
    public void handleMixEnd(MixStreamEndCallback callback) {
        RoomDTO roomDTO = roomService.getRoomByRoomId(Long.valueOf(callback.getBizRoomId()));
        if (roomDTO == null) {
            log.error("handleMixStart error, roomDTO is null, callback={}", JSON.toJSONString(callback));
            return;
        }
        streamService.stopMixStream(callback);
    }


    protected boolean isMixed(RoomConfig roomConfig, ProductEnum platform) {
        log.info("isMixed roomConfig={}", JSON.toJSONString(roomConfig));
        if (roomConfig == null) {
            return isPullMixedMode(platform.name());
        } else {
            return roomConfig.getPullMode().equals(PullModeEnum.MIXED.name());
        }
    }

}
