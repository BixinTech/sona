package cn.bixin.sona.server.room.generator;

import cn.bixin.sona.server.room.domain.enums.ProductEnum;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import org.springframework.stereotype.Component;

/**
 * @author yuanye
 * @date 2020/3/23 4:05 下午
 */
@Component
public class StreamIdGenerator {

    public ProductEnum getPlatform(String streamId) {
        StreamContext context = StreamContext.convert(streamId);
        if (context != null) {
            return ProductEnum.getPlatformByName(context.getProductCode());
        }
        return null;
    }
}
