package cn.bixin.sona.web.response;

import java.io.Serializable;

public class ImTypeInfoVO implements Serializable {
    private static final long serialVersionUID = -1169370251960867817L;

    private String imType;
    private String imRoomId;

    public String getImType() {
        return imType;
    }

    public void setImType(String imType) {
        this.imType = imType;
    }

    public String getImRoomId() {
        return imRoomId;
    }

    public void setImRoomId(String imRoomId) {
        this.imRoomId = imRoomId;
    }
}
