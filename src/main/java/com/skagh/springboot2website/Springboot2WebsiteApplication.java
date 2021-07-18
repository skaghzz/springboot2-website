package com.skagh.springboot2website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Springboot2WebsiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot2WebsiteApplication.class, args);
	}

}
