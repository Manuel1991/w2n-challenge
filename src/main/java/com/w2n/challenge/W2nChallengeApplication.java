package com.w2n.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class W2nChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(W2nChallengeApplication.class, args);
	}

}
