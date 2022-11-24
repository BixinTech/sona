package cn.bixin.sona.gateway;

import cn.bixin.sona.gateway.cat.MercuryStatCollector;
import cn.bixin.sona.gateway.netty.NettyServer;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.dianping.cat.status.StatusExtensionRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;

/**
 * @author qinwei
 */
@Slf4j
@EnableApolloConfig
@EnableScheduling
@SpringBootApplication(scanBasePackages = "cn.bixin.*")
public class SonaGatewayApplication implements CommandLineRunner {

    public static final long SERVER_START_TIME = System.currentTimeMillis();

    @Resource
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(SonaGatewayApplication.class, args);
        StatusExtensionRegister.getInstance().register(new MercuryStatCollector());
        log.info("SonaGatewayApplication start ...");
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}
