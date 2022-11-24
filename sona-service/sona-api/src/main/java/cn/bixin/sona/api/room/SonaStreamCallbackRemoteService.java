package cn.bixin.sona.api.room;

import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateReplayCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.request.callback.MixStreamEndCallback;
import cn.bixin.sona.request.callback.MixStreamStartCallback;

public interface SonaStreamCallbackRemoteService {

    /**
     * 处理创建回调
     */
    Response<Boolean> handleCreateStreamCallback(CreateStreamCallback request);

    /**
     * 处理关闭流回调
     *
     * @param callback: {@link CloseStreamCallback}
     * @return: true success, otherwise false
     */
    Response<Boolean> handleCloseStreamCallback(CloseStreamCallback callback);

    /**
     * 处理回放回调
     *
     * @param callback: {@link CreateReplayCallback}
     * @return: true success, otherwise false
     */
    Response<Boolean> handleCreateReplayCallback(CreateReplayCallback callback);

    /**
     * 处理混流开始回调
     *
     * @param callback: {@link CloseStreamCallback}
     * @return: true success, otherwise false
     */
    Response<Boolean> handleMixStreamStartCallback(MixStreamStartCallback callback);

    /**
     * 处理混流结束回调
     *
     * @param callback: {@link CreateReplayCallback}
     * @return: true success, otherwise false
     */
    Response<Boolean> handleMixStreamEndCallback(MixStreamEndCallback callback);
}
