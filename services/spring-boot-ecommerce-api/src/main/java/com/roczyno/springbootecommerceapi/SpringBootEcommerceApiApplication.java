package com.roczyno.springbootecommerceapi;

import com.roczyno.springbootecommerceapi.entity.Role;
import com.roczyno.springbootecommerceapi.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringBootEcommerceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootEcommerceApiApplication.class, args);

	}
	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder()
						.name("USER")
						.build());
			}
		};
	}

}
