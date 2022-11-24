package cn.bixin.sona.demo.web.message;

import lombok.Data;

@Data
public class MicMessage {

    private String roomId;
    private String uid;
    private int index;
    private int operate;
}
