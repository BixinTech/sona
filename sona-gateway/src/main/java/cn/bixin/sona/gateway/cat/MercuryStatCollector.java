package cn.bixin.sona.gateway.cat;

import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.RoomChannelManager;
import com.dianping.cat.Cat;
import com.dianping.cat.status.AbstractCollector;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinwei
 */
@Slf4j
public class MercuryStatCollector extends AbstractCollector {

    @Override
    public String getId() {
        return "mercury.stat";
    }

    @Override
    public Map<String, String> getProperties() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("conn.auth.count", String.valueOf(NettyChannel.authChannelCount()));
        map.put("conn.unAuth.count", String.valueOf(NettyChannel.unAuthChannelCount()));

        Map<Integer, AtomicInteger> channelTypeCount = NettyChannel.channelTypeCount();
        for (Map.Entry<Integer, AtomicInteger> entry : channelTypeCount.entrySet()) {
            Map<String, String> tags = new HashMap<>();
            tags.put("t", String.valueOf(entry.getKey()));
            Cat.logMetricForCount("mercury.conn.auth-", entry.getValue().get(), tags);
        }
        Cat.logMetricForCount("mercury.conn.unAuth-", NettyChannel.unAuthChannelCount());

        map.put("netty.used.direct.mem", String.valueOf(PlatformDependent.usedDirectMemory()));

        collectRoomChannelManagerStat(RoomChannelManager.MANAGER_FOR_CHATROOM);

        return map;
    }

    private void collectRoomChannelManagerStat(RoomChannelManager manager) {
        RoomChannelManager.SimpleStat roomStat = manager.stat();
        if (roomStat != null) {
            Cat.logMetricForCount("mercury.room." + manager.getName() + ".total", roomStat.getRoomCount());
            Cat.logMetricForCount("mercury.room." + manager.getName() + ".conn", roomStat.getTotalRoomChannelCount());
        }
        RoomChannelManager.DetailStat detailStat = manager.detailStat();
        String eventType = "Mercury.RoomConn." + manager.getName();
        for (Map.Entry<String, Integer> entry : detailStat.getRoomChannelCount().entrySet()) {
            Cat.logBatchEvent(eventType, entry.getKey(), entry.getValue(), 0);
        }
    }
}
