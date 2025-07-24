package com.lms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.lms.api.model.User;
import com.lms.api.repository.UserRepository;

@SpringBootApplication
public class LmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				userRepository.save(new User("admin", "admin@example.com"));
				System.out.println("Admin user created with username 'admin' and email ");
			} else {
				System.out.println("Users already exist in the database.");
				
			}
		};
	}

}
