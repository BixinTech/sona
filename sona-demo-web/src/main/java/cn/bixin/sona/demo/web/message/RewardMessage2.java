package cn.bixin.sona.demo.web.message;

import lombok.Data;

@Data
public class RewardMessage2 {

    private String roomId;
    private String fromUid;
    private String toUid;
    private int giftId;
    private String giftName;
}
