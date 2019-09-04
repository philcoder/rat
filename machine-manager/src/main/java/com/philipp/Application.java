package com.philipp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * For access: http://localhost:15000/manager/<controllers paths>
 * 
 * @author phili
 *
 */
@EnableScheduling
@SpringBootApplication
public class Application {

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}

}