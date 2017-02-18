package com.hyunjae.coati;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoatiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoatiApplication.class, args);
	}
}
