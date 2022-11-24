package cn.bixin.sona.gateway.channel.support;

import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.RoomChannelManager;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.CommandEnum;
import cn.bixin.sona.gateway.common.Header;
import cn.bixin.sona.gateway.common.HeaderEnum;
import cn.bixin.sona.gateway.concurrent.counter.CounterService;
import cn.bixin.sona.gateway.service.SocketNotifyService;
import cn.bixin.sona.gateway.util.AccessMessageUtils;
import cn.bixin.sona.gateway.util.Constants;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.netty.util.Recycler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author qinwei
 * <p>
 * 使用 netty 对象池 ，减少对象的创建
 */
@Slf4j
public class RoomMessageTask implements Runnable {

    private static final Recycler<RoomMessageTask> RECYCLER = new Recycler<RoomMessageTask>() {
        @Override
        protected RoomMessageTask newObject(Handle<RoomMessageTask> handle) {
            return new RoomMessageTask(handle);
        }
    };

    private final Recycler.Handle<RoomMessageTask> handle;

    private RoomChannelManager.RoomInfo roomInfo;

    private JSONObject json;

    private RoomMessageTask(Recycler.Handle<RoomMessageTask> handle) {
        this.handle = handle;
    }

    public static RoomMessageTask newTask(JSONObject json, RoomChannelManager.RoomInfo roomInfo) {
        RoomMessageTask task = RECYCLER.get();
        task.roomInfo = roomInfo;
        task.json = json;
        return task;
    }

    private void recycle() {
        roomInfo = null;
        json = null;
        handle.recycle(this);
    }

    @Override
    public void run() {
        String room = roomInfo.getName();
        int cmd = json.getInteger(Constants.MQ_SEND_KEY_CMD);
        MonitorUtils.newTransaction("CHATROOM.Message", room + ":" + cmd, () -> handTask(room, cmd), (transaction, s) -> recycle());
    }

    private void handTask(String room, int cmd) throws Exception {
        try {
            CounterService.increment(room);
            JSONArray members = json.getJSONArray(Constants.MQ_REPORT_KEY_MEMBERS);
            if (members == null) {
                sendAllMember(room, cmd);
            } else {
                sendAssignMember(room, members, cmd);
            }
        } catch (Exception t) {
            throw new RuntimeException("RoomMessageTask error, room : " + room + ", message : " + json, t);
        }
    }

    private void sendAssignMember(String room, JSONArray members, int cmd) {
        IntStream.range(0, members.size()).mapToObj(members::getString)
                .filter(member -> !CollectionUtils.isEmpty(roomInfo.getChannelsByMember(member)))
                .forEach(member -> sendMember(room, cmd, member, roomInfo.getChannelsByMember(member)));
    }

    private void sendAllMember(String room, int cmd) {
        sendMember(room, cmd, null, roomInfo.getChannels());
        closeRoomIfNeeded(room, cmd);
    }

    private void sendMember(String room, int cmd, String member, Set<NettyChannel> channels) {
        AccessMessage msg = buildMessage(room, cmd, false);
        Set<String> ackUids = getAckUids();
        AccessMessage ackMsg = null;
        if (!CollectionUtils.isEmpty(ackUids)) {
            ackMsg = buildMessage(room, cmd, true);
        }
        boolean priority = Optional.ofNullable(json.getBoolean(Constants.MQ_REPORT_KEY_PRIORITY)).orElse(false);
        //如果是高优先级消息，或者不属于高频房间，立即发送
        boolean immediate = priority || !CounterService.compute(room);
        for (NettyChannel channel : channels) {
            //ack消息 立即发送
            if (ackUids.contains(channel.getUid())) {
                channel.fastSend(ackMsg, true);
            } else {
                channel.fastSend(msg, immediate);
            }
            removeChannelIfNeeded(room, cmd, member, channel);
        }
    }

    private AccessMessage buildMessage(String room, int cmd, boolean ack) {
        AccessMessage message = AccessMessageUtils.createRequest(cmd, json.getString(Constants.MQ_REPORT_KEY_DATA).getBytes(StandardCharsets.UTF_8));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constants.CHATROOM_MSG_KEY_ROOM, room);
        if (ack) {
            jsonObject.put(Constants.CHATROOM_MSG_KEY_ACK, 1);
        }
        if (CommandEnum.CHATROOM_SIGNAL.getCommand() == cmd) {
            jsonObject.put(Constants.CHATROOM_MSG_KEY_SIGNAL, json.getInteger(Constants.CHATROOM_MSG_KEY_SIGNAL));
        }
        message.addHeader(new Header(HeaderEnum.CHATROOM, jsonObject.toJSONString()));
        return message;
    }

    private Set<String> getAckUids() {
        JSONArray ackUids = json.getJSONArray(Constants.MQ_REPORT_KEY_ACK_UIDS);
        if (CollectionUtils.isEmpty(ackUids)) {
            return Collections.emptySet();
        }
        return new HashSet<>(ackUids.toJavaList(String.class));
    }

    private void removeChannelIfNeeded(String room, int cmd, String member, NettyChannel channel) {
        if (CommandEnum.CHATROOM_SIGNAL.getCommand() == cmd && 1 == json.getInteger(Constants.CHATROOM_MSG_KEY_SIGNAL)) {
            SpringApplicationContext.getBean(SocketNotifyService.class).notifyChatRoomSession(channel, cmd, room, member);
            RoomChannelManager.MANAGER_FOR_CHATROOM.removeChannel(room, channel);
        }
    }

    private void closeRoomIfNeeded(String room, int cmd) {
        if (CommandEnum.CHATROOM_SIGNAL.getCommand() == cmd && 2 == json.getInteger(Constants.CHATROOM_MSG_KEY_SIGNAL)) {
            RoomChannelManager.MANAGER_FOR_CHATROOM.destroyRoom(room);
        }
    }

}
