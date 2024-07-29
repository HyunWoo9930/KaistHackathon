package org.example.factorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FactorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(FactorialApplication.class, args);
	}

}
