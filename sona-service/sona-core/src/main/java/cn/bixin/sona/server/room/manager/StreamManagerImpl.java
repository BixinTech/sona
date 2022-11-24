package cn.bixin.sona.server.room.manager;

import cn.bixin.sona.common.dto.Code;
import cn.bixin.sona.common.enums.PullMode;
import cn.bixin.sona.common.enums.RoomStatus;
import cn.bixin.sona.common.exception.YppRunTimeException;
import cn.bixin.sona.dto.AppInfoDTO;
import cn.bixin.sona.dto.RoomDTO;
import cn.bixin.sona.dto.StreamConfigInfoDTO;
import cn.bixin.sona.dto.SupplierConfigDTO;
import cn.bixin.sona.request.ChangeStreamRequest;
import cn.bixin.sona.request.MixMVRequest;
import cn.bixin.sona.server.exception.ExceptionCode;
import cn.bixin.sona.server.room.domain.convert.MixConfigConverter;
import cn.bixin.sona.server.room.domain.convert.StreamConfigInfoConverter;
import cn.bixin.sona.server.room.domain.db.ProductConfig;
import cn.bixin.sona.server.room.domain.db.RoomConfig;
import cn.bixin.sona.server.room.domain.db.Stream;
import cn.bixin.sona.server.room.domain.enums.PlatformEnum;
import cn.bixin.sona.server.room.domain.enums.StreamSupplierEnum;
import cn.bixin.sona.server.room.domain.stream.StreamContext;
import cn.bixin.sona.server.room.domain.stream.UserSginInputParam;
import cn.bixin.sona.server.room.service.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class StreamManagerImpl implements StreamManager {
    private static final Logger log = LoggerFactory.getLogger(StreamManagerImpl.class);

    @Resource
    private StreamService streamService;
    @Resource
    private RoomManagementService roomManagementService;
    @Resource
    private RoomService roomService;
    @Resource
    private ProductConfigService productConfigService;
    @Resource
    private MessageService messageService;
    @Resource
    private MixConfigService mixConfigService;

    @Override
    public StreamConfigInfoDTO createStreamConfig(ProductConfig config, RoomDTO room, long uid, boolean isGuest) {
        StreamConfigInfoDTO streamConfig = StreamConfigInfoConverter.convertStreamConfig(config);
        buildStreamId(streamConfig, config, room.getRoomId(), uid);
        buildAudioToken(streamConfig, config, uid);
        buildStreamUrl(streamConfig, config, room.getRoomId());
        buildStreamRoomId(streamConfig, room);
        buildSwitchSpeaker(streamConfig);
        buildAppInfo(streamConfig, config, uid, isGuest);

        return streamConfig;
    }

    @Override
    public String initStream(long roomId, long uid) {
        if (roomManagementService.isUserStreamPushForBid(roomId, uid)) {
            throw new YppRunTimeException(ExceptionCode.STREAM_PUSH_FORBID);
        }
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = getProductConfig(roomId, room.getProductCode());

        return streamService.initStream(roomId, uid, config);
    }

    @Override
    public String getRoomStreamUrl(long roomId) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = getProductConfig(roomId, room.getProductCode());
        return streamService.getPlayUrl(roomId, config.getStreamSupplier());
    }

    @Override
    public Map<Long, String> batchGetRoomStreamUrls(List<Long> roomIds) {
        Map<Long, RoomDTO> roomMap = roomService.batchGetRooms(roomIds);
        if (MapUtils.isEmpty(roomMap)) {
            return Maps.newHashMap();
        }
        Map<Long, RoomConfig> roomConfigMap = productConfigService.getRoomConfigBatch(roomIds);
        Map<Long, String> result = Maps.newHashMap();
        roomMap.forEach((roomId, room) -> {
            RoomConfig config = roomConfigMap.get(roomId);
            String supplier = config != null ? config.getStreamSupplier() : productConfigService.getConfigInfoByCode(room.getProductCode()).getStreamSupplier();
            result.put(roomId, streamService.getPlayUrl(roomId, supplier));
        });
        return result;
    }

    @Override
    public boolean muteStream(long roomId, List<Long> targetUids, boolean setMute) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = getProductConfig(roomId, room.getProductCode());
        Map<Long, Stream> streamMap = streamService.batchGetUserLivingStream(roomId, targetUids);
        if (MapUtils.isEmpty(streamMap)) {
            return false;
        }
        messageService.sendSonaMuteStreamMessage(roomId, Lists.newArrayList(streamMap.values()), setMute, config.getImModule());
        return true;
    }

    @Override
    public boolean muteRoomStream(long roomId) {
        RoomDTO room = roomService.getRoomByRoomId(roomId);
        checkRoomValid(room);
        ProductConfig config = getProductConfig(roomId, room.getProductCode());
        List<Stream> streams = streamService.getRoomLivingStream(roomId);
        if (CollectionUtils.isEmpty(streams)) {
            return false;
        }
        messageService.sendSonaMuteStreamMessage(roomId, streams, true, config.getImModule());
        return true;
    }

    @Override
    public boolean addStream(ChangeStreamRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        StreamContext context = convertStreamContext(request.getStreamId());
        if (context == null) {
            throw new YppRunTimeException(ExceptionCode.PARSE_STREAM_ERROR);
        }
        return streamService.addStream(context);
    }

    @Override
    public boolean closeStream(ChangeStreamRequest request) {
        RoomDTO room = roomService.getRoomByRoomId(request.getRoomId());
        checkRoomValid(room);
        StreamContext context = convertStreamContext(request.getStreamId());
        if (context == null) {
            throw new YppRunTimeException(ExceptionCode.PARSE_STREAM_ERROR);
        }
        return streamService.closeStream(context.getStreamId());
    }


    private StreamContext convertStreamContext(String streamId) {
        if (StringUtils.isBlank(streamId)) {
            return null;
        }

        String[] streamArray = streamId.split("_");
        if (streamArray.length != 7) {
            return null;
        }
        ProductConfig config = productConfigService.getConfigInfoByShortCode(streamArray[0]);
        if (config == null) {
            return null;
        }

        StreamContext context = new StreamContext();
        context.setStreamId(streamId);
        context.setProductCode(config.getProductCode());
        context.setStreamSupplier(StreamSupplierEnum.getByDesc(streamArray[1]));
        context.setSonaSdkVersion(streamArray[2]);
        context.setPlatform(PlatformEnum.getPlatformByCode(Integer.parseInt(streamArray[3])));
        context.setRoomId(Long.valueOf(streamArray[4]));
        context.setUid(Long.parseLong(streamArray[5]));
        context.setRandomNumber(streamArray[6]);
        context.setShortProductCode(streamArray[0]);
        return context;
    }


    private ProductConfig getProductConfig(long roomId, String productCode) {
        RoomConfig roomConfig = productConfigService.getRoomConfig(roomId);
        return roomConfig != null ? ProductConfig.convertConfigInfo(roomConfig, productCode) : productConfigService.getConfigInfoByCode(productCode);
    }

    private void checkRoomValid(RoomDTO room) {
        if (room == null) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }
        if (room.getStatus() == RoomStatus.INVALID.getCode()) {
            throw new YppRunTimeException(ExceptionCode.ROOM_CLOSED_ERROR);
        }
    }

    private void buildStreamId(StreamConfigInfoDTO streamConfig, ProductConfig config, long roomId, long uid) {
        if (isTencentMulti(config)) {
            streamConfig.setStreamId(streamService.initStream(roomId, uid, getProductConfig(roomId, config.getProductCode())));
        } else {
            streamConfig.setStreamId(String.valueOf(roomId));
        }
    }

    private void buildStreamUrl(StreamConfigInfoDTO streamConfig, ProductConfig config, long roomId) {
        streamConfig.setStreamUrl(streamService.getPlayUrl(roomId, config.getStreamSupplier()));
    }

    private void buildAudioToken(StreamConfigInfoDTO streamConfig, ProductConfig config, long uid) {
        if (isTencent(config)) {
            UserSginInputParam userSginInputParam = StreamConfigInfoConverter.convertUserSignInputParam(config, uid, false);
            streamConfig.setAudioToken(streamService.genUserSig(userSginInputParam).getAppSign());
        }
    }

    private void buildStreamRoomId(StreamConfigInfoDTO streamConfig, RoomDTO room) {
        streamConfig.setStreamRoomId(getStreamRoomIdMap(room));
    }

    private Map<String, String> getStreamRoomIdMap(RoomDTO roomDTO) {
        Map<String, String> streamIdMap = Maps.newHashMap();
        streamIdMap.put(StreamSupplierEnum.ZEGO.name(), String.valueOf(roomDTO.getExt().get(StreamSupplierEnum.ZEGO.name())));
        streamIdMap.put(StreamSupplierEnum.TENCENT.name(), String.valueOf(roomDTO.getExt().get(StreamSupplierEnum.TENCENT.name())));
        return streamIdMap;
    }

    private void buildSwitchSpeaker(StreamConfigInfoDTO streamConfig) {
        streamConfig.setSwitchSpeaker("0");
    }

    private void buildAppInfo(StreamConfigInfoDTO streamConfig, ProductConfig config, long uid, boolean isGuest) {
        UserSginInputParam userSginInputParam = StreamConfigInfoConverter.convertUserSignInputParam(config, uid, isGuest);
        streamConfig.setAppInfo(streamService.genUserSig(userSginInputParam));
    }

    private boolean isTencent(ProductConfig config) {
        return StreamSupplierEnum.TENCENT.name().equals(config.getStreamSupplier());
    }

    private boolean isTencentMulti(ProductConfig config) {
        return config.getPullMode().equals(PullMode.MULTI.name()) && StreamSupplierEnum.TENCENT.name().equals(config.getStreamSupplier());
    }

    @Override
    public AppInfoDTO genUserSig(long roomId, long uid) {
        RoomConfig roomConfig = productConfigService.getRoomConfig(roomId);
        if (roomConfig == null) {
            return null;
        }

        ProductConfig productConfig = new ProductConfig();
        BeanUtils.copyProperties(roomConfig, productConfig);
        UserSginInputParam userSginInputParam = StreamConfigInfoConverter.convertUserSignInputParam(productConfig, uid,false);

        return streamService.genUserSig(userSginInputParam);
    }

    @Override
    public SupplierConfigDTO syncRoomConfig(long roomId) {
        RoomDTO roomDTO = roomService.getRoomByRoomId(roomId);
        if (Objects.isNull(roomDTO)) {
            throw new YppRunTimeException(ExceptionCode.CHATROOM_NOT_EXISTS);
        }

        RoomConfig productConfig = productConfigService.getRoomConfig(roomId);
        if (productConfig == null) {
            return null;
        }

        SupplierConfigDTO supplierConfig = new SupplierConfigDTO();
        supplierConfig.setRoomId(String.valueOf(roomId));

        StreamConfigInfoDTO streamConfigDTO = new StreamConfigInfoDTO();
        streamConfigDTO.setSupplier(productConfig.getStreamSupplier());
        streamConfigDTO.setStreamUrl(streamService.getPlayUrl(roomId, productConfig.getStreamSupplier()));
        streamConfigDTO.setPullMode(productConfig.getPullMode());
        streamConfigDTO.setPushMode(productConfig.getPushMode());
        streamConfigDTO.setBitrate(productConfig.getBitrate());
        streamConfigDTO.setType(productConfig.getType());
        //云商为腾讯时不会使用该streamId字段
        streamConfigDTO.setStreamId(String.valueOf(roomId));
        supplierConfig.setStreamConfig(streamConfigDTO);

        return supplierConfig;
    }

    public boolean mixedMV(MixMVRequest request) {
        Long roomId = request.getRoomId();
        Long uid = request.getUid();

        RoomDTO roomDTO = roomService.getRoomByRoomId(roomId);
        if (Objects.isNull(roomDTO)) {
            log.info("room not exist, roomId: {}", roomId);
            throw new YppRunTimeException(Code.ERROR_PARAM);
        }

        RoomConfig productConfig = productConfigService.getRoomConfig(roomId);
        if (Objects.isNull(productConfig) || !StreamSupplierEnum.ZEGO.name().equals(
            productConfig.getStreamSupplier())) {
            log.info("stream supplier does not match, roomId:{}, productConfig: {}", request.getRoomId(), productConfig);
            return false;
        }

        //开始混流, 设置MV参数, 保幂等
        if (request.getMixStatus() == 1) {
            log.info("mix status is {}, start to execute mix mv operation", 1);
            mixConfigService.createMixConfig(MixConfigConverter
                .defaultVideoMixConfig(roomId, String.valueOf(uid), request.getWidth(), request.getHeight()));
        }

        //停止混入MV信息
        if (request.getMixStatus() == 2) {
            log.info("mix status is {}, start to execute mix mv operation", 2);
            mixConfigService.updateMixConfig(
                MixConfigConverter.defaultAudioMixConfig(roomId, String.valueOf(uid)));
        }

        //开始混流
        //zegoService.doMix(request.getRoomId(), bizRoomId, envService.isTest(), roomDTO.getAppId());

        return true;
    }


}
