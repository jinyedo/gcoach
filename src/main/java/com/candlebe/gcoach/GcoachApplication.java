package com.candlebe.gcoach;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GcoachApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcoachApplication.class, args);
	}

}
