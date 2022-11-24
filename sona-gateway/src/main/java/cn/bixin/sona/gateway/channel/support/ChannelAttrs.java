package cn.bixin.sona.gateway.channel.support;

import cn.bixin.sona.gateway.concurrent.counter.SystemClock;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.ConcurrentHashSet;

import java.net.InetSocketAddress;
import java.util.Set;

/**
 * mercury自定义的channel扩展属性
 *
 * @author qinwei
 */
@Slf4j
@Getter
@Setter
public class ChannelAttrs {

    public static final AttributeKey<ChannelAttrs> MERCURY_ATTRS = AttributeKey.valueOf("MERCURY.ATTRS");

    private final long createTime;

    private final String channelId;

    /**
     * 连接类型
     */
    private int channelType = -1;
    /**
     * 客户端协议版本
     */
    private int clientProtoVer;
    /**
     * 设备id
     */
    private String deviceId;

    private int platform = -1;

    private String sysVer;

    private String model;

    private String uid;

    /**
     * 是否游客模式
     */
    private boolean isVisitor = false;

    private Set<String> rooms = new ConcurrentHashSet<>(2);

    private boolean foreground = true;

    public ChannelAttrs(InetSocketAddress remoteAddr) {
        this.createTime = SystemClock.currentTimeMillis();
        this.channelId = ChannelIdGenerator.generateChannelId(remoteAddr, createTime);
    }

    public static ChannelAttrs init(Channel ch, InetSocketAddress remoteAddr) {
        ChannelAttrs attrs = new ChannelAttrs(remoteAddr);
        attrs = ch.attr(MERCURY_ATTRS).setIfAbsent(attrs);
        return attrs;
    }

    public static ChannelAttrs getAttrs(Channel ch) {
        ChannelAttrs attrs = ch.attr(MERCURY_ATTRS).get();
        if (attrs == null) {
            throwIllegalStateException("MERCURY_ATTRS is null! ch=" + ch);
        }
        return attrs;
    }

    public static ChannelAttrs getAttrsIfExists(io.netty.channel.Channel ch) {
        return ch.attr(MERCURY_ATTRS).get();
    }

    public static String getChannelId(Channel ch) {
        ChannelAttrs attrs = getAttrs(ch);
        String chId = attrs.getChannelId();
        if (StringUtils.isBlank(chId)) {
            throwIllegalStateException("CH_ID is blank! ch=" + ch);
        }
        return chId;
    }

    private static void throwIllegalStateException(String msg) {
        IllegalStateException e = new IllegalStateException(msg);
        log.error(msg, e);
        throw e;
    }

}
