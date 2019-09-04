package com.philipp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * For access: http://localhost:15000/manager/<controllers paths>
 * 
 * @author phili
 *
 */
@SpringBootApplication
public class Application {

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}
}