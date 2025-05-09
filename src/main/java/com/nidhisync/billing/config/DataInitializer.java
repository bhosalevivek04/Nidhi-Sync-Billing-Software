package com.nidhisync.billing.config;

import java.util.List;
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
    public CommandLineRunner loadData(RoleRepository roleRepo,
                                      UserRepository userRepo,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            // Predefined roles
            List<String> roleNames = List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_CLERK");

            // Ensure each role exists
            for (String roleName : roleNames) {
                try {
                    roleRepo.findByName(roleName).orElseGet(() -> {
                        System.out.println("Inserting role: " + roleName);
                        return roleRepo.save(Role.builder().name(roleName).build());
                    });
                } catch (Exception e) {
                    System.err.println("⚠️ Could not insert role '" + roleName + "': " + e.getMessage());
                }
            }

            // Ensure default admin exists
            if (userRepo.findByUsername("admin").isEmpty()) {
                try {
                    Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("ROLE_ADMIN missing"));
                    User admin = User.builder()
                            .username("admin")
                            .email("admin@example.com")
                            .mobileNumber("+911234567890")
                            .password(passwordEncoder.encode("admin123"))
                            .roles(Set.of(adminRole))
                            .build();
                    userRepo.save(admin);
                    System.out.println("✅ Default admin user created.");
                } catch (Exception e) {
                    System.err.println("⚠️ Could not create default admin: " + e.getMessage());
                }
            }
        };
    }
}
