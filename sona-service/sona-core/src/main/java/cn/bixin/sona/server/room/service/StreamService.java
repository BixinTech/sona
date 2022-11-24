package cn.bixin.sona.server.room.service;

import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateReplayCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.request.callback.MixStreamEndCallback;
import cn.bixin.sona.request.callback.MixStreamStartCallback;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.Stream;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.domain.stream.UserSginInputParam;

import java.util.List;
import java.util.Map;

public interface StreamService {
    /**
     * 初始化流
     */
    String initStream(long roomId, long uid, ProductConfig config);

    /**
     * 获取用户有效的流
     *
     * @param uid: uid
     * @return streamId
     */
    Stream getUserLivingStream(long roomId, long uid);

    /**
     * 根据streamId获取stream对象
     *
     * @param streamId: streamId
     * @return stream info
     */
    Stream getStreamByStreamId(String streamId);


    /**
     * 批量获取用户有效流
     * @param roomId
     * @param uids
     * @return
     */
    Map<Long, Stream> batchGetUserLivingStream(long roomId, List<Long> uids);

    List<Stream> getRoomLivingStream(long roomId);

    boolean addStream(StreamContext streamContext);

    boolean closeStream(String streamId);

    /**
     * 获取房间内单流的播放地址
     *
     * @param roomId
     * @param supplier
     * @return play url
     */
    String getPlayUrl(long roomId, String supplier);

    AppInfoDTO genUserSig(UserSginInputParam userSginInputParam);

    /**
     * 处理推流回调
     *
     * @param callback: {@link CreateStreamCallback}
     */
    boolean createStreamCallback(CreateStreamCallback callback);

    /**
     * 处理关流回调
     *
     * @param callback: {@link CreateStreamCallback}
     */
    boolean closeStreamCallback(CloseStreamCallback callback);

    /**
     * 处理回放回调
     *
     * @param callback: {@link CreateStreamCallback}
     */
    void replayStreamCallback(CreateReplayCallback callback);

    /**
     * 处理 混流录制
     * @param callback
     */
    boolean handleMixReplay(CreateReplayCallback callback);

    void addMixStream(MixStreamStartCallback callback, long roomId);

    void stopMixStream(MixStreamEndCallback callback);
}
