package cn.bixin.sona.console;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qinwei
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = { "cn.bixin.sona.*" })
public class SonaConsoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SonaConsoleApplication.class, args);
		log.info("SonaConsoleApplication start ...");
	}

}
