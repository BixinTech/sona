package cn.bixin.sona.server.room.service;

import cn.bixin.sona.request.callback.CloseStreamCallback;
import cn.bixin.sona.request.callback.CreateReplayCallback;
import cn.bixin.sona.request.callback.CreateStreamCallback;
import cn.bixin.sona.request.callback.MixStreamEndCallback;
import cn.bixin.sona.request.callback.MixStreamStartCallback;
import cn.bixin.sona.server.room.domain.stream.StreamContext;

public interface StreamOperation {

    void handleCreate(CreateStreamCallback callback);

    void handleClose(CloseStreamCallback callback);

    void handleReplay(CreateReplayCallback callback);

    void handleAdd(StreamContext context);

    void handleClose(StreamContext context);

    void handleMixStart(MixStreamStartCallback callback);

    void handleMixEnd(MixStreamEndCallback callback);


}
