package cn.bixin.sona.gateway.channel.support;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.handler.ChannelHandler;
import cn.bixin.sona.gateway.common.AccessMessage;
import io.netty.util.Recycler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinwei
 */
@Slf4j
public class ChannelEventTask implements Runnable {

    private static final Recycler<ChannelEventTask> RECYCLER = new Recycler<ChannelEventTask>() {
        @Override
        protected ChannelEventTask newObject(Handle<ChannelEventTask> handle) {
            return new ChannelEventTask(handle);
        }
    };

    private final Recycler.Handle<ChannelEventTask> handle;

    private ChannelHandler handler;

    @Getter
    private NettyChannel channel;

    private ChannelEventState state;

    private Throwable exception;

    private Object message;

    private ChannelEventTask(Recycler.Handle<ChannelEventTask> handle) {
        this.handle = handle;
    }

    public static ChannelEventTask newInstance(ChannelHandler handler, NettyChannel channel, ChannelEventState state) {
        return newInstance(handler, channel, state, null);
    }

    public static ChannelEventTask newInstance(ChannelHandler handler, NettyChannel channel, ChannelEventState state, Throwable exception) {
        return newInstance(handler, channel, state, exception, null);
    }

    public static ChannelEventTask newInstance(ChannelHandler handler, NettyChannel channel, ChannelEventState state, Object message) {
        return newInstance(handler, channel, state, null, message);
    }

    public static ChannelEventTask newInstance(ChannelHandler handler, NettyChannel channel, ChannelEventState state, Throwable exception, Object message) {
        ChannelEventTask eventTask = RECYCLER.get();
        eventTask.handler = handler;
        eventTask.channel = channel;
        eventTask.state = state;
        eventTask.exception = exception;
        eventTask.message = message;
        return eventTask;
    }

    private void recycle() {
        handler = null;
        channel = null;
        state = null;
        exception = null;
        message = null;
        handle.recycle(this);
    }

    @Override
    public void run() {
        try {
            //绝大数请求都是 receive ，减少判断次数
            if (state == ChannelEventState.RECEIVE) {
                AccessMessage msg = (AccessMessage) message;
                MonitorUtils.newTransaction(MonitorUtils.CAT_IN_TRANS_TYPE, String.valueOf(msg.getCmd()), () -> handler.receive(channel, message));
            } else {
                switch (state) {
                    case SENT:
                        handler.send(channel, message);
                        break;
                    case CONNECT:
                        MonitorUtils.newTransaction(MonitorUtils.CAT_IN_TRANS_TYPE, "Active", () -> handler.connect(channel));
                        break;
                    case DISCONNECT:
                        MonitorUtils.newTransaction(MonitorUtils.CAT_IN_TRANS_TYPE, "Inactive", () -> handler.disconnect(channel));
                        break;
                    case CAUGHT:
                        handler.caught(channel, exception);
                        break;
                    default:
                        log.warn("unknown state: " + state + ", message is " + message);
                }
            }
        } catch (Throwable t) {
            log.error("ChannelEventTask handle " + state + " operation error, channel is " + channel, t);
        } finally {
            recycle();
        }
    }

}
