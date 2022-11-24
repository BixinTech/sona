package cn.bixin.sona.server.room.service;

import cn.bixin.sona.server.room.domain.db.MixConfig;

/**
 * @author 木欣
 * @create 2021-05-27
 * @email baojiawei@bixin.cn
 * create by intelliJ IDEA
 */
public interface MixConfigService {

    /**
     * 创建混流配置
     *
     * @param mixConfig
     */
    void createMixConfig(MixConfig mixConfig);

    /**
     * 更新混流配置
     *
     * @param defaultAudioMixConfig
     */
    void updateMixConfig(MixConfig defaultAudioMixConfig);

    /**
     * 通过房间ID获取混流参数
     *
     * @param roomId
     * @return
     */
    MixConfig getMixConfigByRoomId(Long roomId);

    /**
     * 获取混流配置, 如果为空走默认音频配置.
     *
     * @param roomId
     * @return
     */
    MixConfig getMixConfigOrDefaultAudioByRoomId(Long roomId);


}
