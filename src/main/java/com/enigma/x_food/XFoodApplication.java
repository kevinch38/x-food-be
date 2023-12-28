package com.enigma.x_food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class XFoodApplication {
	public static void main(String[] args) {
		SpringApplication.run(XFoodApplication.class, args);
	}
}