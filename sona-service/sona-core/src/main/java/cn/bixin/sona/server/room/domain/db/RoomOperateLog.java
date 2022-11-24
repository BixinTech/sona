package cn.bixin.sona.server.room.domain.db;

import cn.bixin.sona.server.room.domain.enums.IMModuleEnum;

public class RoomOperateLog {
    private Long roomId;
    private String type;
    private String operate;
    private String operator;

    public static RoomOperateLog wrapRoomLog(long roomId, String imModule, String operate, String operator) {
        RoomOperateLog log = new RoomOperateLog();
        log.setRoomId(roomId);
        log.setType(IMModuleEnum.CHATROOM.name());
        log.setOperate(operate);
        log.setOperator(operator);

        return log;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
