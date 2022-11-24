package cn.bixin.sona.console.mapper;

import cn.bixin.sona.console.domain.db.Stream;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface StreamMapper {
    List<Stream> selectStream(@Param("streamId") String streamId,
                              @Param("roomId") Long roomId,
                              @Param("uid") Long uid,
                              @Param("startTime") Date startTime,
                              @Param("endTime") Date endTime,
                              @Param("anchor") Long anchor,
                              @Param("limit") int limit);

    List<Stream> selectStreamInfo(@Param("streamId") String streamId,
                                  @Param("roomId") Long roomId,
                                  @Param("uid") Long uid,
                                  @Param("startTime") Date startTime,
                                  @Param("endTime") Date endTime);
}
