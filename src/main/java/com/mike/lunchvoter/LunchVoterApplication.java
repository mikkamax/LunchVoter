package com.mike.lunchvoter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LunchVoterApplication {

	public static void main(String[] args) {
		SpringApplication.run(LunchVoterApplication.class, args);
	}

}
