package cn.bixin.sona.server.room.domain.convert;

import cn.bixin.sona.server.room.domain.db.MixConfig;

/**
 * @author 木欣
 * @create 2021-05-27
 * @email baojiawei@bixin.cn
 * create by intelliJ IDEA
 */
public class MixConfigConverter {

    /**
     * 创建默认视频配置
     *
     * @param roomId
     * @param width
     * @param height
     * @return
     */
    public static MixConfig defaultVideoMixConfig(Long roomId, String uid, Integer width, Integer height) {
        MixConfig mixConfig = new MixConfig();
        mixConfig.setRoomId(roomId);
        mixConfig.setUid(uid);
        mixConfig.setWidth(width);
        mixConfig.setHeight(height);
        mixConfig.setTops(0);
        mixConfig.setBottom(height);
        mixConfig.setLefts(0);
        mixConfig.setRights(width);
        mixConfig.setBitrate(1200000);
        mixConfig.setFps(15);
        mixConfig.setStatus(1);
        return mixConfig;
    }

    /**
     * 创建默认音频设置
     *
     * @param roomId
     * @param uid
     * @return
     */
    public static MixConfig defaultAudioMixConfig(Long roomId, String uid) {
        MixConfig mixConfig = new MixConfig();
        mixConfig.setRoomId(roomId);
        mixConfig.setUid(uid);
        mixConfig.setWidth(16);
        mixConfig.setHeight(16);
        mixConfig.setTops(0);
        mixConfig.setBottom(16);
        mixConfig.setLefts(0);
        mixConfig.setRights(16);
        mixConfig.setBitrate(1);
        mixConfig.setFps(15);
        mixConfig.setStatus(2);
        return mixConfig;
    }

    /**
     * 在视频混流状态
     *
     * @param mixConfig
     * @return
     */
    public static boolean inMVStatus(MixConfig mixConfig) {
        return 1 == mixConfig.getStatus();
    }

    /**
     * 在音频混流状态
     *
     * @param mixConfig
     * @return
     */
    public static boolean inAudioStatus(MixConfig mixConfig) {
        return 2 == mixConfig.getStatus();
    }

}
