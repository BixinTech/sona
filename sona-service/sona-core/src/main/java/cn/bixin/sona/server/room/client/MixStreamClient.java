package cn.bixin.sona.server.room.client;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketTimeoutException;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

@Component
public class MixStreamClient {

    private static final Logger log = LoggerFactory.getLogger(MixStreamClient.class);

    @Resource(name = "mixHttpClient")
    private OkHttpClient okHttpClient;

    public String sendPost(String url, String param) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, param);
        Request request = new Request.Builder().url(url).addHeader(CONTENT_TYPE, "application/json").post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (SocketTimeoutException e) {
            log.error("zego.sendPost SocketTimeoutException", e);
            return "{code:-889}";
        } catch (IOException e) {
            log.error("zego.sendPost exception", e);
        }
        return StringUtils.EMPTY;
    }

    public String sendGet(String url, String param) {
        Request request = new Request.Builder().url(url + '?' + param).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("zego.sendGet exception", e);
        }
        return null;
    }
}
