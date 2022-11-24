package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomMapper {

    int insertSelective(Room record);

    /**
     * 打开房间
     *
     * @param roomId: roomId
     */
    void openRoom(@Param("roomId") long roomId);

    /**
     * 关闭
     *
     * @param roomId: roomId
     */
    int closeRoom(@Param("roomId") long roomId);

    /**
     * 根据roomId查找聊天室信息
     *
     * @param roomId: 房间id
     *
     * @return chatroom info
     */
    Room getRoomByRoomId(@Param("roomId") long roomId);

    /**
     * 更新密码
     *
     * @param roomId: 房间id(主键)
     * @param password: new password
     */
    int updatePassword(@Param("roomId") Long roomId, @Param("password") String password);

    /**
     * 根据roomId查找聊天室信息
     *
     * @param roomIds: 房间id
     *
     * @return chatroom info
     */
    List<Room> getRoomsByRoomIds(@Param("roomIds") List<Long> roomIds);

    /**
     * 分页查询在线房间
     * @param anchor: roomId
     * @param limit
     * @return
     */
    List<Room> selectOnlineRoomList(@Param("anchor") String anchor, @Param("limit") int limit);

    /**
     * 获取在线房间数量
     * @return
     */
    int countOnlineRoomList();

}
