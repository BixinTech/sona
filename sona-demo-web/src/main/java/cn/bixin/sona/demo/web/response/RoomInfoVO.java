package cn.bixin.sona.demo.web.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class RoomInfoVO implements Serializable {

    private static final long serialVersionUID = -186689155951935278L;

    private String roomId;
    private String name;
    private List<SeatInfoVO> seatList;

}
