package cn.bixin.sona.server.room.manager;

import cn.bixin.sona.common.enums.PullMode;
import cn.bixin.sona.common.enums.RoomStatus;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.common.spring.SpringApplicationContext;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.enums.RoomMixedEnum;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;
import cn.bixin.sona.server.room.domain.enums.IMModuleEnum;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.service.*;
import cn.bixin.sona.server.room.utils.StreamUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Lazy
@Component
public class AudioSwitchManager {

    private static final Logger log = LoggerFactory.getLogger(AudioSwitchManager.class);

    @Resource
    private ProductConfigService productConfigService;
    @Resource
    private RoomService roomService;
    @Resource
    private GroupService groupService;
    @Resource
    private ChatroomService chatroomService;
    @Resource
    private MessageService messageService;

    @Resource
    private StreamService streamService;

    /**
     * 默认码率 96k
     */
    private static final int DEFAULT_BITRATE = 96000;
    /**
     * 默认播放器类型, 1: 三方 2: 自建
     */
    private static final int DEFAULT_PLAYER_TYPE = 1;

    public boolean roomSwitch(List<Long> roomIds, RoomMixedEnum roomMixedEnum) {
        log.info("roomSwitch roomIds:{}, roomMixedEnum:{}", roomIds, roomMixedEnum);

        if (CollectionUtils.isEmpty(roomIds) || roomMixedEnum == null) {
            return false;
        }

        ProductConfig productConfig = getConfigInfoByRoomId(roomIds.get(0));
        if (productConfig == null) {
            return false;
        }

        List<Long> roomIdsWithPeople = getRoomIdsWithPeople(roomIds);
        doRoomSwitchWithPeople(roomIdsWithPeople, roomMixedEnum, productConfig);

        roomIds.removeAll(roomIdsWithPeople);
        doRoomSwitchWithNoPeople(roomIds, roomMixedEnum, productConfig);
        return true;
    }

    private ProductConfig getConfigInfoByRoomId(Long roomId) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        String productCode = config.getProductCode();

        //优先通过房间拿配置
        RoomConfig roomConfig = productConfigService.getRoomConfig(roomId);
        return roomConfig != null ? ProductConfig.convertConfigInfo(roomConfig, productCode) : productConfigService.getConfigInfoByCode(productCode);
    }

    private void doRoomSwitchWithNoPeople(List<Long> roomIds, RoomMixedEnum roomMixedEnum, ProductConfig productConfig) {
        updateAudioConfigInfo(roomIds, roomMixedEnum, productConfig);
    }

    private List<Long> getRoomIdsWithPeople(List<Long> roomIds) {
        Map<Long, Long> memberCount = batchGetRoomMemberCount(roomIds);
        return memberCount.entrySet().stream().filter(e -> e.getValue() > 0L).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private void doRoomSwitchWithPeople(List<Long> roomIds, RoomMixedEnum roomMixedEnum, ProductConfig productConfig) {
        updateAudioConfigInfo(roomIds, roomMixedEnum, productConfig);
        sendSonaSwitchAudioSupplierMessage(roomIds, roomMixedEnum.getCode());
        needToStopMixStream(roomIds, productConfig, roomMixedEnum);
    }

    public void sendSonaSwitchAudioSupplierMessage(List<Long> roomIds, int mixed) {
        log.info("sendSonaSwitchAudioSupplierMessage, roomIds: {}, mixed: {}", roomIds, mixed);

        Map<Long, RoomConfig> productConfigMap = productConfigService.getRoomConfigBatch(roomIds);

        roomIds.forEach(roomId -> {
            Map<String, Object> contentMap = Maps.newHashMap();

            RoomMixedEnum roomMixedEnum = RoomMixedEnum.getRoomMixedEnum(mixed);
            if (roomMixedEnum == null) {
                return;
            }

            RoomConfig roomConfig = productConfigMap.get(roomId);

            contentMap.put("pullMode", RoomMixedEnum.getPullMode(mixed));
            contentMap.put("pushMode", RoomMixedEnum.getPushMode(mixed));
            contentMap.put("supplier", RoomMixedEnum.getSupplier(mixed));
            contentMap.put("roomId", roomId);
            //设置码率
            contentMap.put("bitrate", Objects.isNull(roomConfig) ? DEFAULT_BITRATE : roomConfig.getBitrate());
            //设置播放器类型
            contentMap.put("playerType", DEFAULT_PLAYER_TYPE);

            String streamRoomId = String.valueOf(roomId);

            if (roomMixedEnum == RoomMixedEnum.MIXED_ZEGO_MIXED || roomMixedEnum == RoomMixedEnum.MIXED_TECENT_MIXED) {
                contentMap.put("streamId", streamRoomId);
                if (roomConfig != null) {
                    contentMap.put("streamUrl", streamService.getPlayUrl(roomId, roomConfig.getStreamSupplier()));
                }
            }

            messageService.sendSonaHotSwitchMessage(roomId, contentMap, roomConfig.getImModule());
        });
    }

    private void updateAudioConfigInfo(List<Long> roomIds, RoomMixedEnum roomMixedEnum, ProductConfig productConfig) {
        if (CollectionUtils.isEmpty(roomIds) || roomMixedEnum == null || productConfig == null) {
            return;
        }

        //批量查询t_room_config
        Map<Long, RoomConfig> roomConfigMap = productConfigService.getRoomConfigBatch(roomIds);
        //注意: 不支持单独切码率
        if (!StreamUtil.isProductStreamEqual(productConfig, roomMixedEnum)) {
            List<Long> addRoomIds = getAddRoomIds(roomIds, roomConfigMap);
            productConfigService.addRoomConfig(wrapNewRoomConfig(addRoomIds, roomMixedEnum, productConfig));
        }

        if (!CollectionUtils.isEmpty(roomConfigMap)) {
            productConfigService.updateRoomConfigAudioStreams(
                    wrapUpdateRoomConfig(Lists.newArrayList(roomConfigMap.values()), roomMixedEnum));
        }
    }

    public Map<Long, Long> batchGetRoomMemberCount(List<Long> roomIds) {
        RoomDTO room = roomService.getRoomByRoomId(roomIds.get(0));
        checkRoomValid(room);
        ProductConfig config = productConfigService.getConfigInfoByCode(room.getProductCode());
        if (IMModuleEnum.CHATROOM.name().equals(config.getImModule())) {
            return chatroomService.batchGetChatroomUserCount(roomIds);
        } else {
            return groupService.batchGetGroupMemberCount(roomIds);
        }
    }

    private void checkRoomValid(RoomDTO room) {
        if (room == null) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }
        if (room.getStatus() == RoomStatus.INVALID.getCode()) {
            throw new YppRunTimeException(ExceptionCode.ROOM_CLOSED_ERROR);
        }
    }

    private List<Long> getAddRoomIds(List<Long> roomIds, Map<Long, RoomConfig> roomConfigMap) {
        List<Long> addRoomIds = Lists.newArrayList();
        if (CollectionUtils.isEmpty(roomConfigMap)) {
            addRoomIds = roomIds;
        } else {
            for (Long each : roomIds) {
                if (!roomConfigMap.containsKey(each)) {
                    addRoomIds.add(each);
                }
            }
        }

        return addRoomIds;
    }

    private List<RoomConfig> wrapNewRoomConfig(List<Long> roomIds, RoomMixedEnum roomMixedEnum,
                                               ProductConfig productConfig) {
        List<RoomConfig> list = Lists.newArrayList();
        roomIds.forEach(each -> {
            RoomConfig roomConfig = RoomConfig.convertByProductConfig(productConfig, roomMixedEnum);
            roomConfig.setRoomId(each);
            list.add(roomConfig);
        });

        return list;
    }

    private static List<RoomConfig> wrapUpdateRoomConfig(List<RoomConfig> roomConfigs, RoomMixedEnum roomMixedEnum) {
        List<RoomConfig> list = Lists.newArrayList();
        roomConfigs.forEach(each -> list.add(RoomConfig.convertByRoomConfig(each, roomMixedEnum)));

        return list;
    }

    /**
     * 是否需要停止混流
     *
     * @param roomIds
     * @param productConfig
     * @param roomMixedEnum
     */
    private void needToStopMixStream(List<Long> roomIds, ProductConfig productConfig, RoomMixedEnum roomMixedEnum) {
        log.info("needToStopMixStream, roomIds:{}, streamSupplier:{}, pullMode:{}, roomMixed:{}", roomIds,
                productConfig.getStreamSupplier(), productConfig.getPullMode(), roomMixedEnum);
        //zego 切换到 单流 或者 切换到 tencent 都需要停止混流
        boolean zegoMix = isZegoMix(productConfig);
        boolean nonEqualStream = !StreamUtil.isProductStreamEqual(productConfig, roomMixedEnum);

        if (zegoMix && nonEqualStream) {
            for (Long roomId : roomIds) {
                SpringApplicationContext.getBean(ZegoService.class).stopMix(String.valueOf(roomId));
            }
        }
    }

    /**
     * 是否是zego混流
     *
     * @param productConfig
     * @return
     */
    private boolean isZegoMix(ProductConfig productConfig) {
        return StreamSupplierEnum.ZEGO.name().equals(productConfig.getStreamSupplier()) &&
                PullMode.MIXED.name().equals(productConfig.getPullMode());
    }

}
