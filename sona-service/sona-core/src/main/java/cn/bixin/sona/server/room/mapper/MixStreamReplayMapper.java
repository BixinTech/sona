package cn.bixin.sona.server.room.mapper;

import java.util.Date;
import java.util.List;

import cn.bixin.sona.server.room.domain.db.MixStreamReplay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MixStreamReplayMapper {

    /**
     * 添加记录
     * @param record
     * @return
     */
    int insert(MixStreamReplay record);

    /**
     * 分页查询房间混流录制
     * @param bizRoomId
     * @param beginTime
     * @param endTime
     * @param start
     * @param limit
     * @return
     */
    List<MixStreamReplay> findMixStreamReplayByRoomId(@Param("bizRoomId") String bizRoomId,
        @Param("beginTime") Date beginTime, @Param("endTime") Date endTime, @Param("start") int start,
        @Param("limit") int limit);

    /**
     * 查询总数量
     * @param bizRoomId
     * @param beginTime
     * @param endTime
     * @return
     */
    long findCountMixStreamReplayByRoomId(@Param("bizRoomId") String bizRoomId, @Param("beginTime") Date beginTime,
        @Param("endTime") Date endTime);
}