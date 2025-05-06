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
	public CommandLineRunner loadData(
	    RoleRepository roleRepo,
	    UserRepository userRepo,
	    PasswordEncoder passwordEncoder
	) {
	  return args -> {
	    if (roleRepo.count() == 0) {
	      Role userRole  = roleRepo.save(Role.builder().name("ROLE_USER").build());
	      Role adminRole = roleRepo.save(Role.builder().name("ROLE_ADMIN").build());
	      System.out.println("Default roles inserted");
	    }

	    if (userRepo.count() == 0) {
	      Role userRole = roleRepo.findById(1L).orElseThrow();
	      User admin = User.builder()
	                       .username("admin")
	                       .email("admin@example.com")
	                       .password(passwordEncoder.encode("admin123"))
	                       .roles(Set.of(userRole))
	                       .build();
	      userRepo.save(admin);
	      System.out.println("Test user ‘admin’ inserted");
	    }
	  };
	}

}
