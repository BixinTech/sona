package cn.bixin.sona.gateway.util;

import com.google.common.net.InetAddresses;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.util.Enumeration;

/**
 * @author qinwei
 */
@Slf4j
public final class NetUtil {

    public static final String LOCAL_IP_ADDR = getLocalIpAddress().trim();

    private NetUtil() {
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("IP地址获取失败 {}", e.toString());
        }
        return "";
    }

    public static String getIpAddr(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) socketAddress;
            return addr.getAddress().getHostAddress();
        }
        return "UNKNOWN";
    }

    public static int getPort(SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) socketAddress;
            return addr.getPort();
        }
        return -1;
    }

    public static InetSocketAddress getWsRemoteAddrFromHeader(HttpHeaders requestHeaders, Channel ch) {
        InetSocketAddress remoteAddr = (InetSocketAddress) ch.remoteAddress();
        try {
            String ipForwarded = requestHeaders.get("X-Forwarded-For");
            if (StringUtils.isNotBlank(ipForwarded)) {
                String[] ipArr = ipForwarded.split(",");
                String ip = ipArr[0];
                if ("0:0:0:0:0:0:0:1".equals(ip)) {
                    ip = "127.0.0.1";
                }
                remoteAddr = new InetSocketAddress(InetAddresses.forString(ip.trim()), remoteAddr.getPort());
            }
        } catch (Throwable e) {
            log.error("getWsRemoteAddrFromHeader error! requestHeaders={}", requestHeaders, e);
        }
        return remoteAddr;
    }

}
