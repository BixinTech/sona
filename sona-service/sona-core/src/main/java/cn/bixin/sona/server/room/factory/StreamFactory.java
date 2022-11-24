package cn.bixin.sona.server.room.factory;

import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.service.StreamOperation;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StreamFactory {

    @Resource(name = "zegoStrategy")
    private StreamOperation zegoStrategy;
    @Resource(name = "defaultStrategy")
    private StreamOperation defaultStrategy;
    @Resource(name = "tencentStrategy")
    private StreamOperation tencentStrategy;


    public StreamOperation newInstance(StreamSupplierEnum streamSupplier) {
        if (streamSupplier == StreamSupplierEnum.ZEGO) {
            return zegoStrategy;
        } else if (streamSupplier == StreamSupplierEnum.TENCENT) {
            return tencentStrategy;
        }
        return defaultStrategy;
    }
}
