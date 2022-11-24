package cn.bixin.sona.demo.web.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class SeatInfoVO implements Serializable {
    private static final long serialVersionUID = 6814210990399791771L;

    private int index;
    private String uid;


}
