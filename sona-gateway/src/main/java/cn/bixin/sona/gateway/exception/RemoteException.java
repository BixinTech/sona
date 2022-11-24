package cn.bixin.sona.gateway.exception;

import cn.bixin.sona.gateway.channel.Channel;
import lombok.Getter;

import java.net.InetSocketAddress;

/**
 * @author qinwei
 */
@Getter
public class RemoteException extends Exception {

    private static final long serialVersionUID = -5072922462535211769L;

    private InetSocketAddress localAddress;

    private InetSocketAddress remoteAddress;

    private Object msgBody;

    public RemoteException(String msg) {
        super(msg);
    }

    public RemoteException(Channel channel, String msg) {
        this(channel == null ? null : channel.getLocalAddress(), channel == null ? null : channel.getRemoteAddress(), msg);
    }

    public RemoteException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String msg) {
        super(msg);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemoteException(Channel channel, Throwable cause) {
        this(channel == null ? null : channel.getLocalAddress(), channel == null ? null : channel.getRemoteAddress(), cause);
    }

    public RemoteException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, Throwable cause) {
        super(cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemoteException(Channel channel, String msg, Throwable cause) {
        this(channel == null ? null : channel.getLocalAddress(), channel == null ? null : channel.getRemoteAddress(), msg, cause);
    }

    public RemoteException(InetSocketAddress localAddress, InetSocketAddress remoteAddress, String msg, Throwable cause) {
        super(msg, cause);
        this.localAddress = localAddress;
        this.remoteAddress = remoteAddress;
    }

    public RemoteException(String msg, Throwable t) {
        super(msg, t);
    }

    public RemoteException(String msg, Throwable t, Object msgBody) {
        super(msg, t);
        this.msgBody = msgBody;
    }
}
