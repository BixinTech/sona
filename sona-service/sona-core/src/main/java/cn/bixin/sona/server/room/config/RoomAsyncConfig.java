package cn.bixin.sona.server.room.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
public class RoomAsyncConfig {

    @Bean
    public Executor mixStreamExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("mix-stream-executor");
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return TtlExecutors.getTtlExecutor(executor);
    }

    @Bean
    public Executor sonaExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(10);
        executor.setThreadNamePrefix("sona-executor");
        // 线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return TtlExecutors.getTtlExecutor(executor);
    }

    public static final Timer MIX_STREAM_TIMER = new HashedWheelTimer(100, TimeUnit.MILLISECONDS, 100);

}
