package cn.bixin.sona.server.room.service.impl;

import cn.bixin.sona.api.im.RouterRoomMessageService;
import cn.bixin.sona.api.im.enums.PriorityEnum;
import cn.bixin.sona.api.im.request.GroupMessageRequest;
import cn.bixin.sona.api.im.request.RoomMessageRequest;
import cn.bixin.sona.server.room.domain.db.Stream;
import cn.bixin.sona.server.room.domain.enums.IMModuleEnum;
import cn.bixin.sona.server.room.domain.enums.SonaMessageTypeEnum;
import cn.bixin.sona.server.room.service.MessageService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    @Resource
    private RouterRoomMessageService routerRoomMessageService;

    @Override
    public void sendSonaCreateRoomMessage(long roomId, long uid, String imModule) {
        log.info("sendSonaCreateRoomMessage roomId:{}, uid:{}", roomId, uid);
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_CREATE_ROOM, roomId, uid, null), SonaMessageTypeEnum.MSG_CREATE_ROOM, PriorityEnum.MEDIUM, imModule);
    }

    @Override
    public void sendSonaCloseRoomMessage(long roomId, long uid, String imModule) {
        log.info("sendSonaCloseRoomMessage roomId:{}, uid:{}", roomId, uid);
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_CLOSE_ROOM, roomId, uid, null), SonaMessageTypeEnum.MSG_CLOSE_ROOM, PriorityEnum.MEDIUM, imModule);
    }

    @Override
    public void sendSonaEnterRoomMessage(long roomId, long uid, String imModule) {
        log.info("sendSonaEnterRoomMessage roomId:{}, uid:{}", roomId, uid);
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_ENTER_ROOM, roomId, uid, null), SonaMessageTypeEnum.MSG_ENTER_ROOM, PriorityEnum.MEDIUM, imModule);
    }

    @Override
    public void sendSonaLeaveRoomMessage(long roomId, long uid, String imModule) {
        log.info("sendSonaLeaveRoomMessage roomId:{}, uid:{}", roomId, uid);
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_LEAVE_ROOM, roomId, uid, null), SonaMessageTypeEnum.MSG_LEAVE_ROOM, PriorityEnum.MEDIUM, imModule);
    }

    @Override
    public void sendSonaMuteMessage(long roomId, long uid, boolean setMute, Integer minute, String imModule) {
        log.info("sendSonaMuteMessage roomId:{}, uid:{}, setMute:{}, minute:{}", roomId, uid, setMute, minute);
        Map<String, Object> extMap = Maps.newHashMap();
        extMap.put("isMute", setMute ? "1" : "0");
        if (setMute && minute != null) {
            extMap.put("duration", String.valueOf(minute));
        }
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_MUTE, roomId, uid, extMap), SonaMessageTypeEnum.MSG_MUTE, PriorityEnum.HIGH, imModule);
    }

    @Override
    public void sendSonaBlockMessage(long roomId, long uid, boolean setBlock, String imModule) {
        log.info("sendSonaBlockMessage roomId:{}, uid:{}, setBlock:{}", roomId, uid, setBlock);
        Map<String, Object> extMap = Maps.newHashMap();
        extMap.put("isBlock", setBlock ? "1" : "0");
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_BLOCK, roomId, uid, extMap), SonaMessageTypeEnum.MSG_BLOCK, PriorityEnum.HIGH, imModule);
    }

    @Override
    public void sendSonaKickMessage(long roomId, long uid, String imModule) {
        log.info("sendSonaKickMessage roomId:{}, uid:{}", roomId, uid);
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_KICK, roomId, uid, null), SonaMessageTypeEnum.MSG_KICK, PriorityEnum.HIGH, imModule);
    }

    @Override
    public void sendSonaAdminMessage(long roomId, long uid, boolean setAdmin, String imModule) {
        log.info("sendSonaAdminMessage roomId:{}, uid:{}, setAdmin:{}", roomId, uid, setAdmin);
        Map<String, Object> extMap = Maps.newHashMap();
        extMap.put("isAdmin", setAdmin ? "1" : "0");
        sendSonaMessage(roomId, uid, wrapMessageContent(SonaMessageTypeEnum.MSG_ADMIN, roomId, uid, extMap), SonaMessageTypeEnum.MSG_ADMIN, PriorityEnum.HIGH, imModule);
    }

    @Override
    public void sendSonaMuteStreamMessage(long roomId, List<Stream> streamList, boolean setMute, String imModule) {
        log.info("sendSonaMuteStreamMessage roomId:{}, streamList:{}, setMute:{}", roomId, JSON.toJSONString(streamList), setMute);
        Map<String, Object> extMap = Maps.newHashMap();
        extMap.put("isMute", setMute ? "1" : "0");
        List<StreamInfo> streams = Lists.newArrayList();
        streamList.forEach(each -> streams.add(new StreamInfo(each.getUid(), each.getStreamId())));
        extMap.put("streamList", streams);
        sendSonaMessage(roomId, 0, wrapMessageContent(SonaMessageTypeEnum.MSG_VOICE, roomId, null, extMap), SonaMessageTypeEnum.MSG_VOICE, PriorityEnum.MEDIUM, imModule);
    }

    @Override
    public void sendSonaHotSwitchMessage(Long roomId, Map<String, Object> contentMap, String imModule) {
        Map<String, Object> map = wrapMessageContent(SonaMessageTypeEnum.MSG_SWITCH_AUDIO, roomId, null, contentMap);
        sendSonaMessage(roomId, 0, map, SonaMessageTypeEnum.MSG_SWITCH_AUDIO, PriorityEnum.HIGH, imModule);
    }

    private static class StreamInfo {
        private long uid;
        private String streamId;

        public StreamInfo(long uid, String streamId) {
            this.uid = uid;
            this.streamId = streamId;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public String getStreamId() {
            return streamId;
        }

        public void setStreamId(String streamId) {
            this.streamId = streamId;
        }
    }

    private static Map<String, Object> wrapMessageContent(SonaMessageTypeEnum messageType, long roomId, Long uid, Map<String, Object> extMap) {
        Map<String, Object> contentMap = Maps.newHashMap();
        if (uid != null) {
            contentMap.put("uid", String.valueOf(uid));
        }
        contentMap.put("roomId", String.valueOf(roomId));
        if (!CollectionUtils.isEmpty(extMap)) {
            contentMap.putAll(extMap);
        }

        Map<String, Object> map = Maps.newHashMap();
        map.put("msgType", messageType.getCode());
        map.put("data", contentMap);
        map.put("roomId", roomId);
        return map;
    }

    private void sendSonaMessage(long roomId, long uid, Map<String, Object> contentMap, SonaMessageTypeEnum msgType, PriorityEnum priority, String imModule) {
        if (IMModuleEnum.CHATROOM.name().equals(imModule)) {
            RoomMessageRequest request = new RoomMessageRequest();
            request.setRoomId(roomId);
            request.setPriority(priority);
            request.setUid(uid);
            request.setMsgType(msgType.getCode());
            request.setContent(JSON.toJSONString(contentMap));
            log.info("send sona chatroom message:{}", JSON.toJSONString(request));
            routerRoomMessageService.sendChatRoomMessage(request);
        } else if (IMModuleEnum.GROUP.name().equals(imModule)) {
            GroupMessageRequest request = new GroupMessageRequest();
            request.setGroupId(roomId);
            request.setUid(uid);
            request.setContent(JSON.toJSONString(contentMap));
            log.info("send sona group message:{}", JSON.toJSONString(request));
            routerRoomMessageService.sendGroupMessage(request);
        }
    }
}
