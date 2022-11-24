package cn.bixin.sona.web.response;

import java.io.Serializable;

public class SupplierConfigVO implements Serializable {

    private static final long serialVersionUID = -8758798373775992578L;
    /**
     * 房间ID
     */
    private String roomId;

    /**
     * 流配置
     */
    private StreamConfigInfoVO streamConfig;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public StreamConfigInfoVO getStreamConfig() {
        return streamConfig;
    }

    public void setStreamConfig(StreamConfigInfoVO streamConfig) {
        this.streamConfig = streamConfig;
    }
}
