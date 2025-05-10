package com.nidhisync.billing.config;

//SecurityConfig.java

import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nidhisync.billing.repository.UserRepository;
import com.nidhisync.billing.util.JwtUtil;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepo) {
		return username -> userRepo.findByUsername(username)
				.map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(),
						user.getPassword(),
						user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName()))
								.collect(Collectors.toList())))
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserDetailsService uds, PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(uds);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationProvider authProvider, JwtUtil jwtUtil)
			throws Exception {
		http.csrf().disable().authenticationProvider(authProvider)
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

						// ADMIN only
						.requestMatchers("/api/users/**").hasRole("ADMIN")

						// CLERK & ADMIN
						.requestMatchers("/api/customers/**").hasAnyRole("CLERK", "ADMIN")
						.requestMatchers("/api/invoices/**").hasAnyRole("CLERK", "ADMIN")

						// All authenticated roles
						.requestMatchers("/api/products/**").hasAnyRole("USER", "CLERK", "ADMIN")
						.requestMatchers("/api/categories/**").hasAnyRole("USER", "CLERK", "ADMIN")
						.requestMatchers("/api/profile/**").authenticated()

						.requestMatchers("/api/analytics/**").hasRole("ADMIN")

						.anyRequest().denyAll())
				.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}