package cn.bixin.sona.gateway.service;

import cn.bixin.sona.gateway.SonaGatewayApplication;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.common.ChannelTypeEnum;
import cn.bixin.sona.gateway.mq.RocketSender;
import cn.bixin.sona.gateway.util.Constants;
import cn.bixin.sona.gateway.util.NetUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@Service
public class SocketNotifyService {

    private static final String TOPIC_SOCKET_ROOM_SESSION = "TOPIC_SOCKET_ROOM_SESSION";

    private static final String TOPIC_ROOM_MESSAGE = "TOPIC_ROOM_MESSAGE";

    private static final String TOPIC_SERVER_STATS = "TOPIC_SERVER_STATS";

    @Resource
    private RocketSender rocketSender;

    public SendResult processConnect(NettyChannel channel) {
        ChannelAttrs attrs = channel.getAttrs();
        if (ChannelTypeEnum.CHATROOM.getType() != attrs.getChannelType()) {
            return null;
        }
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(Constants.MQ_REPORT_KEY_TYPE, Constants.MQ_REPORT_VAL_TYPE_CONNECT);
        jsonParam.put(Constants.MQ_REPORT_KEY_CHANNEL_ID, attrs.getChannelId());
        jsonParam.put(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT, System.currentTimeMillis());
        jsonParam.put(Constants.MQ_REPORT_KEY_DEVICE_ID, attrs.getDeviceId());
        jsonParam.put(Constants.MQ_REPORT_KEY_UID, attrs.getUid());
        jsonParam.put(Constants.MQ_REPORT_KEY_SESSION, Constants.SESSION_ONLINE);
        return rocketSender.syncSend(TOPIC_SOCKET_ROOM_SESSION, null, attrs.getChannelId(), jsonParam.toJSONString());
    }

    public SendResult processDisConnect(NettyChannel channel) {
        ChannelAttrs attrs = channel.getAttrs();
        if (ChannelTypeEnum.CHATROOM.getType() != attrs.getChannelType()) {
            return null;
        }
        String uid = attrs.getUid();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(Constants.MQ_REPORT_KEY_TYPE, Constants.MQ_REPORT_VAL_TYPE_CONNECT);
        jsonParam.put(Constants.MQ_REPORT_KEY_CHANNEL_ID, attrs.getChannelId());
        jsonParam.put(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT, System.currentTimeMillis());
        jsonParam.put(Constants.MQ_REPORT_KEY_SESSION, Constants.SESSION_OFFLINE);
        jsonParam.put(Constants.MQ_REPORT_KEY_UID, uid);
        String hashKey = StringUtils.hasText(uid) ? uid : attrs.getChannelId();
        return rocketSender.syncSend(TOPIC_SOCKET_ROOM_SESSION, null, hashKey, jsonParam.toJSONString());
    }

    public SendResult notifyChatRoomSession(NettyChannel channel, int cmd, String room, String uid) {
        ChannelAttrs attrs = channel.getAttrs();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(Constants.MQ_REPORT_KEY_TYPE, Constants.MQ_REPORT_VAL_TYPE_ROOM);
        jsonParam.put(Constants.MQ_REPORT_KEY_CHANNEL_ID, attrs.getChannelId());
        jsonParam.put(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT, System.currentTimeMillis());
        jsonParam.put(Constants.MQ_REPORT_KEY_ROOM, room);
        jsonParam.put(Constants.MQ_REPORT_KEY_UID, ObjectUtils.defaultIfNull(uid, attrs.getUid()));
        jsonParam.put(Constants.MQ_REPORT_KEY_CMD, cmd);
        return rocketSender.syncSend(TOPIC_SOCKET_ROOM_SESSION, null, uid, jsonParam.toJSONString());
    }

    public void notifyChatRoomMessage(NettyChannel channel, String uid, String room, String body) {
        ChannelAttrs attrs = channel.getAttrs();
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(Constants.MQ_REPORT_KEY_UID, uid);
        jsonParam.put(Constants.MQ_REPORT_KEY_DEVICE_ID, attrs.getDeviceId());
        jsonParam.put(Constants.MQ_REPORT_KEY_CLIENT_IP, NetUtil.getIpAddr(channel.getRemoteAddress()));
        jsonParam.put(Constants.MQ_REPORT_KEY_ROOM, room);
        jsonParam.put(Constants.MQ_REPORT_KEY_DATA, body);
        rocketSender.asyncSend(TOPIC_ROOM_MESSAGE, jsonParam.toJSONString());
    }

    @Scheduled(fixedRate = 5000)
    public void reportServerStats() {
        String serverId = NetUtil.LOCAL_IP_ADDR;
        JSONObject jsonParam = new JSONObject();
        jsonParam.put(Constants.MQ_REPORT_KEY_SERVER_ID, serverId);
        jsonParam.put(Constants.MQ_REPORT_KEY_START_TIME, SonaGatewayApplication.SERVER_START_TIME);
        jsonParam.put(Constants.MQ_REPORT_KEY_TIMESTAMP_SHORT, System.currentTimeMillis());
        jsonParam.put(Constants.MQ_REPORT_KEY_AUTH_CONN, NettyChannel.authChannelCount());
        jsonParam.put(Constants.MQ_REPORT_KEY_UNAUTH_CONN, NettyChannel.unAuthChannelCount());
        rocketSender.syncSend(TOPIC_SERVER_STATS, null, serverId, jsonParam.toJSONString());
    }

}
