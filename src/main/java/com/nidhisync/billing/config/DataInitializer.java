package com.nidhisync.billing.config;

//DataInitializer.java
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nidhisync.billing.entity.Role;
import com.nidhisync.billing.entity.User;
import com.nidhisync.billing.repository.RoleRepository;
import com.nidhisync.billing.repository.UserRepository;

@Configuration
public class DataInitializer {
	@Bean
	public CommandLineRunner loadData(RoleRepository roleRepo, UserRepository userRepo,
			PasswordEncoder passwordEncoder) {
		return args -> {
			if (roleRepo.count() == 0) {
				Role userRole = roleRepo.save(Role.builder().name("ROLE_USER").build());
				Role adminRole = roleRepo.save(Role.builder().name("ROLE_ADMIN").build());
				Role clerkRole = roleRepo.save(Role.builder().name("ROLE_CLERK").build());
				System.out.println("Default roles inserted");
			}

			if (userRepo.count() == 0) {
				Role userRole = roleRepo.findByName("ROLE_USER").orElseThrow();
				Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseThrow();
				User admin = User.builder().username("admin").email("admin@example.com")
						.password(passwordEncoder.encode("admin123")).roles(Set.of(adminRole, userRole)) // ← now has
																											// ROLE_ADMIN
																											// too
						.build();
				userRepo.save(admin);
			}
		};
	}

}
