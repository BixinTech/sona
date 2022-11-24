package cn.bixin.sona.gateway.channel.support;

import cn.bixin.sona.gateway.util.NetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinwei
 */
@Slf4j
public final class ChannelIdGenerator {

    private static final String CH_ID_SEPARATOR = "|";

    private static final AtomicInteger SEQ = new AtomicInteger();

    private ChannelIdGenerator() {
    }

    public static String generateChannelId(InetSocketAddress remoteAddr, long timestamp) {
        StringBuilder sb = new StringBuilder(96);
        sb.append(NetUtil.LOCAL_IP_ADDR);
        sb.append(CH_ID_SEPARATOR);
        sb.append(remoteAddr.getAddress().getHostAddress());
        sb.append(CH_ID_SEPARATOR);
        sb.append(remoteAddr.getPort());
        sb.append(CH_ID_SEPARATOR);
        sb.append(timestamp);
        sb.append(CH_ID_SEPARATOR);
        sb.append(Integer.toHexString(SEQ.getAndIncrement()));
        return sb.toString();
    }

}
