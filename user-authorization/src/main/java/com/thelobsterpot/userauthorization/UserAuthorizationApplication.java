package com.thelobsterpot.userauthorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class UserAuthorizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthorizationApplication.class, args);
	}

	public String hello() {
		return "hello world";
	}
}
