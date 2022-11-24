package cn.bixin.sona.console.manager;

import cn.bixin.sona.common.dto.PageResult;
import cn.bixin.sona.common.util.IdGenerator;
import cn.bixin.sona.console.convert.BeanCreator;
import cn.bixin.sona.console.domain.db.*;
import cn.bixin.sona.console.domain.dto.MediaRoomInfoDTO;
import cn.bixin.sona.console.domain.enums.HotSwitchStatusEnum;
import cn.bixin.sona.console.domain.req.HotSwitchRequest;
import cn.bixin.sona.console.domain.req.MediaRoomRequest;
import cn.bixin.sona.console.facade.StreamServiceFacade;
import cn.bixin.sona.console.service.*;
import cn.bixin.sona.console.utils.CollectionUtil;
import cn.bixin.sona.enums.RoomMixedEnum;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MediaRoomInfoManager {
    @Resource
    private SwitchDetailService switchDetailService;
    @Resource
    private RoomService roomService;
    @Resource
    private ProductConfigService productConfigService;
    @Resource
    private RoomConfigService roomConfigService;
    @Resource
    private SwitchBatchService switchBatchService;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private StreamServiceFacade streamServiceFacade;

    public List<MediaRoomInfoDTO> queryMediaRoomInfo(MediaRoomRequest request) {
        List<Long> roomIds = CollectionUtil.spiltToLong(request.getRoomIds(), ',');
        if (CollectionUtils.isEmpty(roomIds)) {
            List<RoomConfig> roomConfigs = roomConfigService.selectByStreamSupplierAndPullMode(request.getCloudSupplierType(), request.getPullStreamType());
            return roomConfigs.stream().map(BeanCreator::createMediaRoomInfoDTO).collect(Collectors.toList());
        }
        List<Room> rooms = roomService.selectByRoomIds(roomIds);
        Map<Long, String> roomProductMap = rooms.stream().collect(Collectors.toMap(Room::getRoomId, Room::getProductCode, (a, b) -> a));
        List<RoomConfig> roomConfigs = roomConfigService.selectByRoomIds(roomIds);
        List<MediaRoomInfoDTO> mediaRoomInfoDTOS = roomConfigs.stream().map(BeanCreator::createMediaRoomInfoDTO).collect(Collectors.toList());

        List<Long> hitRoomIds = roomConfigs.stream().map(RoomConfig::getRoomId).collect(Collectors.toList());
        roomIds.removeIf(hitRoomIds::contains);
        List<ProductConfig> productConfigs = productConfigService.selectByProductCodes(roomProductMap.values());
        Map<String, ProductConfig> productConfigMap = productConfigs.stream().collect(Collectors.toMap(ProductConfig::getProductCode, a -> a, (a, b) -> a));
        List<MediaRoomInfoDTO> defaultMediaRoomInfoDTO = roomIds.stream().map(i -> BeanCreator.createMediaRoomInfoDTO(i, productConfigMap, roomProductMap)).collect(Collectors.toList());
        mediaRoomInfoDTOS.addAll(defaultMediaRoomInfoDTO);
        return mediaRoomInfoDTOS;
    }

    @SuppressWarnings("UnstableApiUsage")
    public String hotSwitchSelf(HotSwitchRequest request) {
        List<Long> roomIds = CollectionUtil.spiltToLong(request.getRoomIds(), ',');
        RoomMixedEnum roomMixedEnum = RoomMixedEnum.getRoomMixedEnum(request.getSwitchType());
        //数据库添加批次切换记录
        long batchId = idGenerator.id();
        saveBatchOperation(roomMixedEnum.getSupplier(), roomMixedEnum.getPullMode() , batchId);
        RateLimiter rateLimiter = RateLimiter.create(10);
        roomIds.forEach(roomId -> {
            rateLimiter.acquire();
            Boolean flag = streamServiceFacade.switchAudioSupplierRoom(roomId, roomMixedEnum.getCode());
            SwitchDetail switchDetail = new SwitchDetail();
            switchDetail.setBatchId(batchId);
            switchDetail.setRoomId(roomId);
            switchDetail.setStreamSupplier(roomMixedEnum.getSupplier());
            switchDetail.setPullMode(roomMixedEnum.getPullMode());
            switchDetail.setStatus(flag ? 1 : 2);
            switchDetail.setOperator("admin");
            switchDetailService.insertSelective(switchDetail);

        });
        return batchId + "";
    }

    private void saveBatchOperation(String supplier, String pullMode, long batchId) {
        SwitchBatch switchBatch = new SwitchBatch();
        switchBatch.setId(batchId);
        switchBatch.setSwitchType(1);
        switchBatch.setStreamSupplier(supplier);
        switchBatch.setPullMode(pullMode);
        switchBatch.setStatus(0);
        switchBatch.setOperator("admin");
        switchBatchService.insertSelective(switchBatch);
    }


    public PageResult<SwitchBatch> queryHotSwitchRecords(int start, int pageSize) {
        if (start <= 0) { start = 1; }
        if (pageSize <= 0) { pageSize = 10; }
        int offset = (start - 1) * pageSize;

        List<SwitchBatch> switchBatchDTOS = switchBatchService.queryByParam(offset, pageSize);
        if (CollectionUtils.isEmpty(switchBatchDTOS)) {
            return PageResult.newPageResult(Collections.emptyList(), true);
        }
        List<Long> batchIds = switchBatchDTOS.stream().filter(item -> item.getSwitchType() == 1).map(SwitchBatch::getId).collect(Collectors.toList());
        List<SwitchDetail> switchDetails = switchDetailService.queryDetailsByIds(batchIds);
        Map<Long, List<SwitchDetail>> collect = switchDetails.stream().collect(
                Collectors.groupingBy(SwitchDetail::getBatchId));

        switchBatchDTOS.forEach(item -> {
            List<SwitchDetail> resultDetail = collect.get(item.getId());
            if(CollectionUtils.isEmpty(resultDetail)){
                item.setStatus(0);
                return;
            }
            List<SwitchDetail> running = resultDetail.stream().filter(ele -> ele.getStatus() == 0).collect(
                    Collectors.toList());
            List<SwitchDetail> failed = resultDetail.stream().filter(ele -> ele.getStatus() == 2).collect(
                    Collectors.toList());
            if(!org.springframework.util.CollectionUtils.isEmpty(running)){
                item.setStatus(HotSwitchStatusEnum.RUNNING.getCode());
            }else if(!org.springframework.util.CollectionUtils.isEmpty(failed)){
                item.setStatus(HotSwitchStatusEnum.FAILED.getCode());
            }else{
                item.setStatus(HotSwitchStatusEnum.SUCCESS.getCode());
            }
        });
        Integer totalCount = switchBatchService.queryTotalCount();
        PageResult<SwitchBatch> pageResult = new PageResult<>();
        pageResult.setCount((long)totalCount);
        pageResult.setEnd(false);
        pageResult.setList(switchBatchDTOS);
        return pageResult;
    }
}
