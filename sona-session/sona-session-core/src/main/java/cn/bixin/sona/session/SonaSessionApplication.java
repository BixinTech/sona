package cn.bixin.sona.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author qinwei
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = "cn.bixin.*")
public class SonaSessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SonaSessionApplication.class, args);
	}

}
