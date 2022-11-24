package cn.bixin.sona.gateway.channel.handler;

/**
 * @author qinwei
 * <p>
 * ChannelHandler 包装类
 */
public class ChannelHandlerWrap {

    private static final ChannelHandlerWrap INSTANCE = new ChannelHandlerWrap();

    private ChannelHandlerWrap() {
    }

    private static ChannelHandlerWrap getInstance() {
        return INSTANCE;
    }

    public static ChannelHandler wrap(ChannelHandler handler) {
        return getInstance().wrapHandler(handler);
    }

    private ChannelHandler wrapHandler(ChannelHandler handler) {
        return new AccessChannelHandler(new CatReportChannelHandler(new IdleChannelHandler(new DispatchChannelHandler(handler))));
    }
}
