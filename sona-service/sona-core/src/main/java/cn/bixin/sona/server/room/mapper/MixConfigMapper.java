package cn.bixin.sona.server.room.mapper;

import cn.bixin.sona.server.room.domain.db.MixConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 混流配置
 *
 * @author 木欣
 * @create 2021-05-27
 * @email baojiawei@bixin.cn
 * create by intelliJ IDEA
 */
@Mapper
public interface MixConfigMapper {

    /**
     * 插入/更新配置
     *
     * @param config
     * @return
     */
    int insertOrUpdate(MixConfig config);

    /**
     * 更新配置
     *
     * @param mixConfig
     * @return
     */
    int updateMixConfig(MixConfig mixConfig);

    /**
     * 通过房间ID获取混流配置
     *
     * @param roomId
     * @return
     */
    MixConfig getMixConfigByRoomId(Long roomId);
}
