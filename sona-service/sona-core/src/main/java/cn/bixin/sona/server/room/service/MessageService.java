package cn.bixin.sona.server.room.service;

import cn.bixin.sona.server.room.constant.ChatRoomConstant;
import cn.bixin.sona.server.room.domain.db.Stream;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

public interface MessageService {

    /**
     * 发送创建房间消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param imModule
     */
    void sendSonaCreateRoomMessage(long roomId, long uid, String imModule);

    /**
     * 发送关闭房间消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param imModule
     */
    void sendSonaCloseRoomMessage(long roomId, long uid, String imModule);

    /**
     * 发送进入房间消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param imModule
     */
    void sendSonaEnterRoomMessage(long roomId, long uid, String imModule);

    /**
     * 离开进入房间消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param imModule
     */
    void sendSonaLeaveRoomMessage(long roomId, long uid, String imModule);

    /**
     * 设置/取消禁言消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param setMute: true set mute, false cancel mute
     * @param minute: mute minutes, mute forever if minute is null
     *
     */
    void sendSonaMuteMessage(long roomId, long uid, boolean setMute, Integer minute, String imModule);

    /**
     * 设置/取消拉黑消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param setBlock: true set block, false remove block
     *
     */
    void sendSonaBlockMessage(long roomId, long uid, boolean setBlock, String imModule);

    /**
     * 踢人消息
     *
     * @param roomId: roomId
     * @param uid: uid
     */
    void sendSonaKickMessage(long roomId, long uid, String imModule);

    /**
     * 设置/取消管理员消息
     *
     * @param roomId: roomId
     * @param uid: uid
     * @param setAdmin: true set admin, false remove admin
     */
    void sendSonaAdminMessage(long roomId, long uid, boolean setAdmin, String imModule);

    /**
     * 设置/取消静音消息
     *
     * @param roomId: roomId
     * @param streamList: streamList
     * @param setMute: true set mute, otherwise cancel mute
     */
    void sendSonaMuteStreamMessage(long roomId, List<Stream> streamList, boolean setMute, String imModule);


    /**
     * 发送热切消息
     * @param roomId
     * @param contentMap
     * @param imModule
     */
    void sendSonaHotSwitchMessage(Long roomId, Map<String, Object> contentMap, String imModule);
}
