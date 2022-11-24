package cn.bixin.sona.dto;

import java.io.Serializable;

public class SupplierConfigDTO implements Serializable {

    /**
     * 房间ID
     */
    private String roomId;
    /**
     * 流配置
     */
    private StreamConfigInfoDTO streamConfig;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public StreamConfigInfoDTO getStreamConfig() {
        return streamConfig;
    }

    public void setStreamConfig(StreamConfigInfoDTO streamConfig) {
        this.streamConfig = streamConfig;
    }
}
