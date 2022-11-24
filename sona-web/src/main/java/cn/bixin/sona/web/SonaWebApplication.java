package cn.bixin.sona.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qinwei
 */
@SpringBootApplication(scanBasePackages = "cn.bixin.*")
public class SonaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SonaWebApplication.class, args);
    }

}
