package cn.bixin.sona.demo.web.request;

import java.io.Serializable;

import lombok.Data;

@Data
public class RewardRequest implements Serializable {
    private static final long serialVersionUID = 9051969452824439765L;

    private String roomId;
    private String fromUid;
    private String toUid;
    private int giftId;

}
