package cn.bixin.sona.gateway.channel;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.ConcurrentHashSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 管理连接与房间的对应关系
 *
 * @author qinwei
 */
public class RoomChannelManager {

    public static final RoomChannelManager MANAGER_FOR_CHATROOM = new RoomChannelManager("ChatRoom");

    private final Map<String, RoomInfo> roomInfoMap = new ConcurrentHashMap<>();

    @Getter
    private final String name;

    public RoomChannelManager(String name) {
        this.name = name;
    }

    @Data
    public static class RoomInfo {
        private final String name;
        private final Set<NettyChannel> channels;
        private final Map<String, Set<NettyChannel>> memberChannelsMap;

        public RoomInfo(String name) {
            this.name = name;
            this.channels = new ConcurrentHashSet<>();
            this.memberChannelsMap = new ConcurrentHashMap<>();
        }

        public boolean registerUid(String uid, NettyChannel channel) {
            return memberChannelsMap.computeIfAbsent(uid, k -> new ConcurrentHashSet<>(1)).add(channel);
        }

        public boolean deregisterUid(NettyChannel channel) {
            String oldUid = channel.getAttrs().getUid();
            if (StringUtils.isBlank(oldUid)) {
                return false;
            }
            Set<NettyChannel> channels = memberChannelsMap.get(oldUid);
            if (channels != null && channels.remove(channel)) {
                if (channels.isEmpty()) {
                    // destroy if empty
                    memberChannelsMap.computeIfPresent(oldUid, (k, v) -> v.isEmpty() ? null : v);
                }
                return true;
            }
            return false;
        }

        public Set<NettyChannel> getChannelsByMember(String member) {
            return member == null ? Collections.emptySet() : memberChannelsMap.getOrDefault(member, Collections.emptySet());
        }
    }

    public boolean addChannel(String roomName, NettyChannel channel, String uid) {
        RoomInfo roomInfo = roomInfoMap.computeIfAbsent(roomName, RoomInfo::new);
        if (StringUtils.isNotBlank(uid)) {
            roomInfo.registerUid(uid, channel);
        }
        boolean isNewAdded = roomInfo.getChannels().add(channel);
        channel.getAttrs().getRooms().add(roomName);
        return isNewAdded;
    }

    public void removeChannel(String roomName, NettyChannel channel) {
        RoomInfo roomInfo = roomInfoMap.get(roomName);
        if (roomInfo != null) {
            Set<NettyChannel> channels = roomInfo.getChannels();
            channels.remove(channel);
            if (channels.isEmpty()) {
                // destroy room if empty
                roomInfoMap.computeIfPresent(roomName, (k, v) -> v.getChannels().isEmpty() ? null : v);
            }
            roomInfo.deregisterUid(channel);
        }
        channel.getAttrs().getRooms().remove(roomName);
    }

    public void destroyRoom(String roomName) {
        RoomInfo roomInfo = roomInfoMap.remove(roomName);
        if (roomInfo != null) {
            for (NettyChannel channel : roomInfo.getChannels()) {
                channel.getAttrs().getRooms().remove(roomName);
            }
        }
    }

    public RoomInfo getRoomInfo(String roomName) {
        return roomInfoMap.get(roomName);
    }

    public Collection<RoomInfo> getAllRoomInfos() {
        return roomInfoMap.values();
    }


    @Data
    public static class SimpleStat {
        private int roomCount;
        private int totalRoomChannelCount;

        private String maxRoom;
        private int maxRoomChannelCount;
    }

    public SimpleStat stat() {
        SimpleStat stat = new SimpleStat();
        stat.setRoomCount(roomInfoMap.size());

        int totalRoomChannelCount = 0;
        String maxRoom = null;
        int maxRoomChannelCount = 0;
        for (RoomInfo roomInfo : roomInfoMap.values()) {
            int size = roomInfo.getChannels().size();
            totalRoomChannelCount += size;
            if (size > maxRoomChannelCount) {
                maxRoomChannelCount = size;
                maxRoom = roomInfo.getName();
            }
        }
        stat.setTotalRoomChannelCount(totalRoomChannelCount);
        stat.setMaxRoom(maxRoom);
        stat.setMaxRoomChannelCount(maxRoomChannelCount);
        return stat;
    }


    @Data
    public static class DetailStat {
        private Map<String, Integer> roomChannelCount;
    }

    public DetailStat detailStat() {
        DetailStat stat = new DetailStat();
        Map<String, Integer> roomChannelCount = Maps.newHashMapWithExpectedSize(roomInfoMap.size());
        for (RoomInfo roomInfo : roomInfoMap.values()) {
            roomChannelCount.put(roomInfo.getName(), roomInfo.getChannels().size());
        }
        stat.setRoomChannelCount(roomChannelCount);
        return stat;
    }

    @Data
    public static class MemberStat {
        private Map<String, Set<String>> roomMembers;
    }

    public MemberStat memberStat() {
        MemberStat stat = new MemberStat();
        Map<String, Set<String>> roomMembers = Maps.newHashMapWithExpectedSize(roomInfoMap.size());
        for (RoomInfo roomInfo : roomInfoMap.values()) {
            roomMembers.put(roomInfo.getName(), roomInfo.getMemberChannelsMap().keySet());
        }
        stat.setRoomMembers(roomMembers);
        return stat;
    }

}
