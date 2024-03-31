package com.DogFoot.adpotAnimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AdpotAnimalApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdpotAnimalApplication.class, args);
	}

}
