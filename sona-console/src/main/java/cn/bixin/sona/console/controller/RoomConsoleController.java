package cn.bixin.sona.console.controller;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.dto.Response;
import cn.bixin.sona.console.domain.db.SwitchBatch;
import cn.bixin.sona.console.domain.dto.MediaRoomInfoDTO;
import cn.bixin.sona.console.domain.req.HotSwitchRequest;
import cn.bixin.sona.console.domain.req.MediaRoomRequest;
import cn.bixin.sona.console.domain.req.PageQuery;
import cn.bixin.sona.console.manager.MediaRoomInfoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
public class RoomConsoleController {
    @Resource
    private MediaRoomInfoManager mediaRoomInfoManager;

    /**
     * 查询房间配置
     * @return
     */
    @PostMapping("/media/room/info")
    public Response<List<MediaRoomInfoDTO>> queryMediaRoomInfo(@RequestBody MediaRoomRequest request) {
        return Response.success(mediaRoomInfoManager.queryMediaRoomInfo(request));
    }

    /**
     * 手动热切
     * @return
     */
    @PostMapping("/media/room/switch/self")
    public Response<String> hotSwitchSelf(@RequestBody HotSwitchRequest request) {
        return Response.success(mediaRoomInfoManager.hotSwitchSelf(request));
    }

    /**
     * 热切记录
     * @return
     */
    @PostMapping("/media/room/switch/record")
    public Response<PageResult<SwitchBatch>> queryHotSwitchRecords(@RequestBody PageQuery pageQuery) {
        return Response.success(mediaRoomInfoManager.queryHotSwitchRecords(pageQuery.getStart(), pageQuery.getPageSize()));
    }

}
