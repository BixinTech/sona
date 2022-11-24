package cn.bixin.sona.gateway.handler;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.CommandEnum;
import cn.bixin.sona.gateway.exception.RemoteException;
import cn.bixin.sona.gateway.util.Constants;
import cn.bixin.sona.gateway.util.EventRecordLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static cn.bixin.sona.gateway.msg.AccessResponse.*;

/**
 * @author qinwei
 * <p>
 * 处理client端上报消息
 */
@Service("clientPush")
public class ClientPushHandler extends AbstractHandler {

    public static final String CLIENT_PUSH_EVENT = "Client push";

    @Override
    protected Object doHandle(NettyChannel channel, AccessMessage message) throws RemoteException {
        byte[] body = message.getBody();
        if (body == null || body.length == 0) {
            EventRecordLog.logEvent(channel, CLIENT_PUSH_EVENT, message, "Empty body");
            MonitorUtils.logEvent(MonitorUtils.CLIENT_PUSH_PROBLEM, "EmptyBody");
            return EMPTY_BODY;
        }

        if (CommandEnum.CLIENT_PUSH.getCommand() != message.getCmd()) {
            EventRecordLog.logEvent(channel, CLIENT_PUSH_EVENT, message, "Unknown cmd");
            MonitorUtils.logEvent(MonitorUtils.CLIENT_PUSH_PROBLEM, "UnknownCmd");
            return null;
        }

        String bodyData = new String(body, StandardCharsets.UTF_8);
        EventRecordLog.logEvent(channel, CLIENT_PUSH_EVENT, message, bodyData);
        JSONObject jsonObject = JSON.parseObject(bodyData);
        String type = Optional.ofNullable(jsonObject.getString(Constants.PUSH_MSG_KEY_TYPE)).orElse("");
        JSONObject data = jsonObject.getJSONObject(Constants.PUSH_MSG_KEY_DATA);
        switch (type) {
            case Constants.PUSH_MSG_TYPE_APPSTATE:
                if (data == null || !StringUtils.hasText(data.getString(Constants.APPSTATE_KEY_FOREGROUND))) {
                    MonitorUtils.logEvent(MonitorUtils.CLIENT_PUSH_PROBLEM, "InvalidData");
                    return INVALID_DATA;
                }
                channel.getAttrs().setForeground(data.getBooleanValue(Constants.APPSTATE_KEY_FOREGROUND));
                return SUCCESS;
            default:
                MonitorUtils.logEvent(MonitorUtils.CLIENT_PUSH_PROBLEM, "UnknownType");
                return null;
        }
    }

}
