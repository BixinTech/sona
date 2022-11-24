package cn.bixin.sona.gateway.task;

import cn.bixin.sona.gateway.channel.NettyChannel;
import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * @author qinwei
 */
@Slf4j
public abstract class ScheduleTimerTask extends AbstractTimerTask {

    public ScheduleTimerTask(long tick) {
        super(tick);
    }

    @Override
    public void run(Timeout timeout) throws Exception {
        Collection<NettyChannel> allChannels = NettyChannel.getAllChannels();
        for (NettyChannel channel : allChannels) {
            if (channel.isConnected()) {
                doTask(channel);
            }
        }
        reput(timeout);
    }

    protected abstract void doTask(NettyChannel channel);
}
