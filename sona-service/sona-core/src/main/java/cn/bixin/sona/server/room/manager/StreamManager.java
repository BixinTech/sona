package cn.bixin.sona.server.room.manager;

import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.StreamConfigInfoDTO;
import cn.bixin.sona.dto.SupplierConfigDTO;
import cn.bixin.sona.request.ChangeStreamRequest;
import cn.bixin.sona.request.MixMVRequest;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.stream.StreamContext;

import java.util.List;
import java.util.Map;

public interface StreamManager {

    StreamConfigInfoDTO createStreamConfig(ProductConfig config, RoomDTO room, long uid, boolean isGuest);

    String initStream(long roomId, long uid);

    String getRoomStreamUrl(long roomId);

    Map<Long, String> batchGetRoomStreamUrls(List<Long> roomIds);

    boolean muteStream(long roomId, List<Long> targetUids, boolean setMute);

    boolean muteRoomStream(long roomId);

    boolean addStream(ChangeStreamRequest request);

    boolean closeStream(ChangeStreamRequest request);

    AppInfoDTO genUserSig(long roomId, long uid);

    SupplierConfigDTO syncRoomConfig(long roomId);

    boolean mixedMV(MixMVRequest request);
}
