package cn.bixin.sona.demo.web.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class MicRequest implements Serializable {

    private String roomId;
    private int index;
    private String uid;
    private int operate; //0-下麦 1-上麦
}
