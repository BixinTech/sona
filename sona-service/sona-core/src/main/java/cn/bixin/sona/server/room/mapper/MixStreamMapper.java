package cn.bixin.sona.server.room.mapper;

import java.util.Date;

import cn.bixin.sona.server.room.domain.db.MixStream;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MixStreamMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MixStream record);

    int insertSelective(MixStream record);

    MixStream selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MixStream record);

    int updateByPrimaryKey(MixStream record);

    int stopMixStream(@Param("streamId") String streamId, @Param("source") int source, @Param("endTime") Date endTime);

    /**
     * 通过streamId 查询 最新一条记录
     * @param streamId
     * @param source
     * @return
     */
    MixStream findLatestByStreamId(@Param("streamId") String streamId, @Param("source") int source);

}