package cn.bixin.sona.gateway.util;

import cn.bixin.sona.gateway.cat.MonitorUtils;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.common.Header;
import cn.bixin.sona.gateway.common.HeaderEnum;
import cn.bixin.sona.gateway.common.Varint;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 * @author qinwei
 */
public final class AccessMessageUtils {

    private AccessMessageUtils() {
    }

    public static AccessMessage createRequest(int cmd, byte[] body) {
        return createRequest(cmd, 0, body);
    }

    public static AccessMessage createRequest(int cmd, int id, byte[] body) {
        return createRequest(cmd, id, false, body);
    }

    public static AccessMessage createRequest(int cmd, int id, boolean twoWay, byte[] body) {
        AccessMessage request = new AccessMessage();
        request.setReq(true);
        request.setTwoWay(twoWay);
        request.setId(id);
        request.setCmd(cmd);
        request.setBody(body);
        return request;
    }

    public static AccessMessage createResponse(int id, int cmd, byte[] body) {
        AccessMessage response = new AccessMessage();
        response.setId(id);
        response.setCmd(cmd);
        response.setBody(body);
        return response;
    }

    public static AccessMessage createHeartResponse(int id) {
        AccessMessage response = new AccessMessage();
        response.setHeartbeat(true);
        response.setId(id);
        return response;
    }

    public static AccessMessage createHeartRequest(int id) {
        AccessMessage request = new AccessMessage();
        request.setReq(true);
        request.setTwoWay(true);
        request.setHeartbeat(true);
        request.setId(id);
        return request;
    }

    public static String extractHeaderData(AccessMessage message, HeaderEnum header) {
        return extractHeaderData(message, header.getType());
    }

    public static String extractHeaderData(AccessMessage message, int headerType) {
        List<Header> headers = message.getHeaders();
        if (CollectionUtils.isEmpty(headers)) {
            return null;
        }
        return headers.stream().filter(header -> header.getType() == headerType).findFirst().map(header -> new String(header.getData(), StandardCharsets.UTF_8)).orElse(null);
    }

    public static void logInboundMsgSize(AccessMessage message, String cmd) {
        logMsgSize(message, cmd, MonitorUtils.CAT_METRIC_IN_SIZE);
    }

    public static void logOutboundMsgSize(AccessMessage message) {
        logMsgSize(message, message.isHeartbeat() ? "HB" : String.valueOf(message.getCmd()), MonitorUtils.CAT_METRIC_OUT_SIZE);
    }

    public static void logMsgSize(AccessMessage message, String cmd, String type) {
        MonitorUtils.logMetricForCount(type, calcMsgSize(message), Collections.singletonMap("cmd", cmd));
    }

    public static int calcMsgSize(AccessMessage message) {
        int size = 4 + Varint.computeRawVarint32Size(message.getId());
        if (!message.isHeartbeat()) {
            int length = message.getLength();
            if (length <= 0) {
                List<Header> headers = message.getHeaders();
                int headerCount = headers == null ? 0 : headers.size();
                int headerLength = 0;
                for (int i = 0; i < headerCount; i++) {
                    headerLength += headers.get(i).calcTotalLength();
                }
                int bodyLength = message.getBody() == null ? 0 : message.getBody().length;
                length = headerLength + bodyLength;
            }
            size += 1 + Varint.computeRawVarint32Size(length) + length;
        }
        return size;
    }

}
