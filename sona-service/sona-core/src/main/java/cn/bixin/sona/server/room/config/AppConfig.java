package cn.bixin.sona.server.room.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class AppConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.dispatcher().setMaxRequestsPerHost(64);
        return okHttpClient;
    }

    @Bean("mixHttpClient")
    public OkHttpClient mixHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(500, TimeUnit.MILLISECONDS)
                .connectTimeout(500, TimeUnit.MILLISECONDS).build();
    }

}
