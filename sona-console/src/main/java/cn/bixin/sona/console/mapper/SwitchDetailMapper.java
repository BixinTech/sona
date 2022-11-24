package cn.bixin.sona.console.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Collection;

import cn.bixin.sona.console.domain.db.SwitchDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SwitchDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SwitchDetail record);

    int insertSelective(SwitchDetail record);

    SwitchDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SwitchDetail record);

    int updateByPrimaryKey(SwitchDetail record);

    List<SwitchDetail> selectAllByBatchIdIn(@Param("batchIdCollection")Collection<Long> batchIdCollection);


}