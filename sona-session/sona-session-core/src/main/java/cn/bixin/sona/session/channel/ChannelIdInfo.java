package cn.bixin.sona.session.channel;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author qinwei
 */
public class ChannelIdInfo {

    public static final String SEPARATOR_STR = "|";
    public static final char SEPARATOR_CHAR = '|';
    public static final Splitter SPLITTER = Splitter.on(SEPARATOR_CHAR);

    private String channelId;

    private String serverId;
    private String remoteAddr;
    private String remotePort;
    private String timestamp;
    private String seqNum;

    public static ChannelIdInfo parseChannelId(String channelId) {
        if (StringUtils.isBlank(channelId)) {
            return null;
        }
        List<String> list = SPLITTER.splitToList(channelId);
        if (list == null || list.size() != 5) {
            return null;
        }

        ChannelIdInfo info = new ChannelIdInfo();
        info.setChannelId(channelId);
        info.setServerId(list.get(0));
        info.setRemoteAddr(list.get(1));
        info.setRemotePort(list.get(2));
        info.setTimestamp(list.get(3));
        info.setSeqNum(list.get(4));
        return info;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(String seqNum) {
        this.seqNum = seqNum;
    }
}
