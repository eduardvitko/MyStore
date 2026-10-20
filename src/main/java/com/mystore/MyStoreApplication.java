package com.mystore;

import com.mystore.entity.RoleEnum;
import com.mystore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyStoreApplication.class, args);
	}
	@Bean
	CommandLineRunner makeAdmin(UserRepository userRepository) {
		return args -> {
			userRepository.findByEmail("eduardvitko@gmail.com").ifPresent(user -> {
				user.setRole(RoleEnum.ADMIN);
				userRepository.save(user);
			});
		};
	}
}
