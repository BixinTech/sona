package cn.bixin.sona.api.room;

import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.dto.SupplierConfigDTO;
import cn.bixin.sona.request.ChangeStreamRequest;
import cn.bixin.sona.request.InitStreamRequest;
import cn.bixin.sona.request.MixMVRequest;

import java.util.List;
import java.util.Map;

public interface StreamRemoteService {

    /**
     * 获取推流ID
     * @param request
     * @return
     */
    Response<String> initStream(InitStreamRequest request);

    /**
     * 获取房间拉流地址
     * @param roomId
     * @return
     */
    Response<String> getRoomStreamUrl(long roomId);

    /**
     * 批量获取房间拉流地址
     * @param roomIds
     * @return
     */
    Response<Map<Long, String>> getRoomStreamUrlBatch(List<Long> roomIds);

    /**
     * 静音用户
     * @param roomId
     * @param targetUids
     * @return
     */
    Response<Boolean> muteStream(long roomId, List<Long> targetUids);

    /**
     * 取消静音用户
     * @param roomId
     * @param targetUids
     * @return
     */
    Response<Boolean> cancelMuteStream(long roomId, List<Long> targetUids);

    /**
     * 静音房间所有用户
     * @param roomId
     * @return
     */
    Response<Boolean> muteRoomStream(long roomId);

    /**
     * 添加流
     * @param request
     * @return
     */
    Response<Boolean> addStream(ChangeStreamRequest request);

    /**
     * 关闭流
     * @param request
     * @return
     */
    Response<Boolean> closeStream(ChangeStreamRequest request);

    /**
     * zego重新混流
     *
     * @param roomId
     * @return
     */
    Response<Boolean> zegoReMix(long roomId);

    /**
     * 生成userSig
     *
     * @return
     */
    Response<AppInfoDTO> genUserSig(long roomId, long uid);

    /**
     * 同步房间配置(记录主动拉配置的用户)
     *
     * @param roomId
     * @return
     */
    Response<SupplierConfigDTO> syncRoomConfigByRoomId(long roomId);

    /**
     * 是否混入mv
     */
    Response<Boolean> mixedMV(MixMVRequest request);
}
