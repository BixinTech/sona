package cn.bixin.sona.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qinwei
 */
@SpringBootApplication(scanBasePackages = "cn.bixin.*")
public class SonaServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(SonaServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SonaServiceApplication.class, args);
        log.info("SonaServiceApplication start ...");
    }

}
