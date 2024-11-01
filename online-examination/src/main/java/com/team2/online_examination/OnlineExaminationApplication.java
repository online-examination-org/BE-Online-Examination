package com.team2.online_examination;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineExaminationApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineExaminationApplication.class, args);
	}

}
