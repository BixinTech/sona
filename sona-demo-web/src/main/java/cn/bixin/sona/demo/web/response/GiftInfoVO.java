package cn.bixin.sona.demo.web.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class GiftInfoVO implements Serializable {
    private static final long serialVersionUID = 1528075310023292375L;

    private int giftId;
    private String giftName;
    private Integer price;
}
