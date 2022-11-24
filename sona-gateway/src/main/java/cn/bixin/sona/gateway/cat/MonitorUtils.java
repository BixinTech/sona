package cn.bixin.sona.gateway.cat;

import cn.bixin.sona.gateway.channel.support.ChannelAttrs;
import cn.bixin.sona.gateway.channel.NettyChannel;
import cn.bixin.sona.gateway.common.AccessMessage;
import cn.bixin.sona.gateway.util.ExecuteFunction;
import cn.bixin.sona.gateway.util.NetUtil;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author qinwei
 */
public final class MonitorUtils {

    public static final String IDLE_STATE_EVENT = "Mercury.IdleStateEvent";
    public static final String PROBE_IDLE = "Mercury.ProbeIdle";
    public static final String CLOSE_LONG_LASTING_CHANNEL = "Mercury.CloseLongLastingChannel";

    public static final String SAFE_EXCEPTION = "SafeException";
    public static final String CONNECTION_RESET_BY_PEER = "SafeException.ConnectionResetByPeer";

    public static final String LOGIN_PROBLEM = "Mercury.LoginProblem";
    public static final String CLIENT_PUSH_PROBLEM = "Mercury.ClientPushProblem";
    public static final String IM_MSG_PROBLEM = "Mercury.IMMsgProblem";
    public static final String CHATROOM_PROBLEM = "Mercury.ChatRoomProblem";
    public static final String SIGNAL_MSG_PROBLEM = "Mercury.SignalMsgProblem";
    public static final String ACTIVITY_MSG_PROBLEM = "Mercury.ActivityMsgProblem";
    public static final String MQ_LISTENER_PROBLEM = "Mercury.MqListenerProblem";
    public static final String IM_MSG_LISTENER_PROBLEM = "Mercury.IMMsgListenerProblem";
    public static final String SEND_MESSAGE_PROBLEM = "Mercury.SendMessageProblem";
    public static final String RECEIVE_MESSAGE_PROBLEM = "Mercury.ReceiveMessageProblem";

    public static final String LOGIN = "Mercury.Login";
    public static final String ACK_RECEIVE = "Mercury.AckReceive";

    public static final String MERCURY_ACCESS_DENY = "Mercury.AccessDeny";

    public static final String CAT_IN_TRANS_TYPE = "ChIn";

    public static final String CAT_OUT_TRANS_TYPE = "ChOut";

    public static final String CAT_METRIC_IN_SIZE = "Msg.In.Size";
    public static final String CAT_METRIC_OUT_SIZE = "Msg.Out.Size";

    private MonitorUtils() {
    }

    public static void logEvent(String type, String name) {
        Cat.logEvent(type, name);
    }

    public static void logBatchEvent(String type, String name, int count, int error) {
        Cat.logBatchEvent(type, name, count, error);
    }

    public static void logMetricForCount(String name, int quantity, Map<String, String> tags) {
        Cat.logMetricForCount(name, quantity, tags);
    }

    public static void logMetricForDuration(String name, long durationInMillis) {
        Cat.logMetricForDuration(name, durationInMillis);
    }

    public static void newTransaction(String type, String name, ExecuteFunction execution) {
        newTransaction(type, name, execution, null);
    }

    public static void newCompletedTransactionWithDuration(String type, String name, long duration) {
        Cat.newCompletedTransactionWithDuration(type, name, duration);
    }

    public static void newTransaction(String type, String name, ExecuteFunction execution, BiConsumer<Transaction, String> finallyExecution) {
        Transaction t = Cat.newTransaction(type, name);
        try {
            execution.execute();
            t.setSuccessStatus();
        } catch (Exception e) {
            t.setStatus(e);
        } finally {
            if (finallyExecution != null) {
                finallyExecution.accept(t, name);
            }
            t.complete();
        }
    }

    public static void logCatEventWithMessage(String type, String name, Object msg, boolean isSuccess) {
        String fullName;
        if (msg instanceof AccessMessage) {
            AccessMessage message = (AccessMessage) msg;
            fullName = Joiner.on(":").join(name, message.getCmd());
        } else {
            fullName = Joiner.on(":").join(name, msg.getClass().getName());
        }
        logBatchEvent(type, fullName, 1, isSuccess ? 0 : 1);
    }

    public static void logCatEventWithChannelAttrs(String type, String name, NettyChannel channel, boolean isSuccess) {
        logCatEventWithChannelAttrs(type, name, channel, isSuccess, false);
    }

    public static void logCatEventWithChannelAttrs(String type, String name, NettyChannel channel, boolean isSuccess, boolean withRemoteAddr) {
        List<String> nameParts = new ArrayList<>();
        if (StringUtils.isNotBlank(name)) {
            nameParts.add(name);
        }

        ChannelAttrs attrs = channel.getAttrsIfExists();
        if (attrs != null) {
            nameParts.add(String.valueOf(attrs.getPlatform()));
            nameParts.add("t" + attrs.getChannelType());
        } else {
            nameParts.add("0");
            nameParts.add("t0");
        }

        if (withRemoteAddr) {
            SocketAddress remoteAddr = channel.getRemoteAddress();
            nameParts.add(NetUtil.getIpAddr(remoteAddr));
            nameParts.add(String.valueOf(NetUtil.getPort(remoteAddr)));
        }

        logCatEventWithComplexName(type, nameParts, isSuccess);
    }

    private static void logCatEventWithComplexName(String type, Iterable<String> nameParts, boolean isSuccess) {
        String fullName = Joiner.on(":").join(nameParts);
        logBatchEvent(type, fullName, 1, isSuccess ? 0 : 1);
    }

}
