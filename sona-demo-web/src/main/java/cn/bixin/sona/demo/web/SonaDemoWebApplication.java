package cn.bixin.sona.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 */
@Slf4j
@SpringBootApplication(scanBasePackages = "cn.bixin.*")
public class SonaDemoWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonaDemoWebApplication.class, args);
        log.info("cn.bixin.sona.demo.web.SonaDemoWebApplication start ...");
    }

}
