package com.candlebe.gcoach;

import com.candlebe.gcoach.storage.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class GcoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcoachApplication.class, args);
	}

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println("현재 시간: " + new Date());
	}

//	@Bean
//	CommandLineRunner init(ContentService contentService) {
//		return (args) -> {
//			contentService.deleteAll();
//			contentService.init();
//		};
//	}
}
