package cn.bixin.sona.server.room.domain.convert;

import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.server.room.domain.db.Room;
import com.alibaba.fastjson.JSON;

import java.util.Map;

public class RoomDTOConverter {

    public static RoomDTO convertDO(Room room) {
        if (room == null) {
            return null;
        }

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomId(room.getRoomId());
        roomDTO.setName(room.getName());
        roomDTO.setStatus(room.getStatus());
        roomDTO.setPassword(room.getPassword());
        roomDTO.setUid(room.getUid());
        roomDTO.setProductCode(room.getProductCode());
        roomDTO.setCreateTime(room.getCreateTime());
        roomDTO.setExt(JSON.parseObject(room.getExt(), Map.class));
        return roomDTO;
    }
}
