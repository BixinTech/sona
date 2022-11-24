package cn.bixin.sona.console.mapper;

import cn.bixin.sona.console.domain.db.SwitchBatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SwitchBatchMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SwitchBatch record);

    int insertSelective(SwitchBatch record);

    SwitchBatch selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SwitchBatch record);

    int updateByPrimaryKey(SwitchBatch record);

    List<SwitchBatch> queryByParam(@Param("start")Integer start, @Param("pageSize")Integer pageSize);

    int queryTotalCount();
}