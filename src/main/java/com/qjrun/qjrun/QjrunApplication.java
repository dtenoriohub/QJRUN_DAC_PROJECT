package com.qjrun.qjrun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QjrunApplication {

	public static void main(String[] args) {
		SpringApplication.run(QjrunApplication.class, args);
	}

}
