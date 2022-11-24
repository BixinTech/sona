package cn.bixin.sona.gateway.common;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinwei
 */
public class AccessMessage {

    private boolean req;

    private boolean twoWay;

    private boolean heartbeat;

    private int version;

    private int id;

    private int cmd;

    private int length;

    private List<Header> headers;

    private byte[] body;

    public AccessMessage() {
    }

    public AccessMessage(AccessMessage message) {
        this.req = message.req;
        this.twoWay = message.twoWay;
        this.heartbeat = message.heartbeat;
        this.version = message.version;
        this.id = message.id;
        this.cmd = message.cmd;
        if (message.headers != null) {
            this.headers = new ArrayList<>(message.headers);
        }
        this.body = message.body;
    }

    public void addHeader(Header header) {
        if (headers == null) {
            headers = new ArrayList<>();
        }
        headers.add(header);
    }

    public boolean isReq() {
        return req;
    }

    public void setReq(boolean req) {
        this.req = req;
    }

    public boolean isTwoWay() {
        return twoWay;
    }

    public void setTwoWay(boolean twoWay) {
        this.twoWay = twoWay;
    }

    public boolean isHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(boolean heartbeat) {
        this.heartbeat = heartbeat;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "AccessMessage{" +
                "req=" + req +
                ", twoWay=" + twoWay +
                ", heartbeat=" + heartbeat +
                ", version=" + version +
                ", id=" + id +
                ", cmd=" + cmd +
                ", length=" + length +
                ", headers=" + headers +
                ", body=" + (body == null ? null : new String(body, StandardCharsets.UTF_8)) +
                '}';
    }
}
