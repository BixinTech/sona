package cn.bixin.sona.server.room.controller;

import cn.bixin.sona.api.room.SonaStreamCallbackRemoteService;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.request.callback.*;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.factory.StreamFactory;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

@DubboService
public class SonaStreamCallbackStreamController implements SonaStreamCallbackRemoteService {

    private static final Logger log = LoggerFactory.getLogger(SonaStreamCallbackStreamController.class);

    @Resource
    private StreamFactory streamFactory;

    @Override
    public Response<Boolean> handleCreateStreamCallback(CreateStreamCallback callback) {
        log.info("handleCreateStreamCallback callback:{}", JSON.toJSONString(callback));
        StreamSupplierEnum streamSupplier = StreamSupplierEnum.getByCode(callback.getSource());
        if (streamSupplier == null) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }

        streamFactory.newInstance(streamSupplier).handleCreate(callback);
        return Response.success(true);

    }

    @Override
    public Response<Boolean> handleCloseStreamCallback(CloseStreamCallback callback) {
        log.info("handleCloseStreamCallback callback:{}", JSON.toJSONString(callback));
        StreamSupplierEnum streamSupplier = StreamSupplierEnum.getByCode(callback.getSource());
        if (streamSupplier == null) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }

        if (StringUtils.isBlank(callback.getErrMsg())) {
            callback.setErrMsg("none");
        }
        streamFactory.newInstance(streamSupplier).handleClose(callback);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> handleCreateReplayCallback(CreateReplayCallback callback) {
        log.info("handleCreateReplayCallback callback:{}", JSON.toJSONString(callback));
        StreamSupplierEnum streamSupplier = StreamSupplierEnum.getByCode(callback.getSource());
        if (streamSupplier == null) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }
        streamFactory.newInstance(streamSupplier).handleReplay(callback);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> handleMixStreamStartCallback(MixStreamStartCallback callback) {
        log.info("handleMixStreamStartCallback callback:{}", JSON.toJSONString(callback));
        StreamSupplierEnum streamSupplier = StreamSupplierEnum.getByCode(callback.getSource());
        if (streamSupplier == null) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }

        streamFactory.newInstance(null).handleMixStart(callback);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> handleMixStreamEndCallback(MixStreamEndCallback callback) {
        log.info("handleMixStreamEndCallback callback:{}", JSON.toJSONString(callback));
        StreamSupplierEnum streamSupplier = StreamSupplierEnum.getByCode(callback.getSource());
        if (streamSupplier == null) {
            throw new YppRunTimeException(ExceptionCode.STREAM_SUPPLIER_NOT_FOUND);
        }

        streamFactory.newInstance(null).handleMixEnd(callback);
        return Response.success(true);
    }
}
