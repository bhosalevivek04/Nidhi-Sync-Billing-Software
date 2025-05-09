package com.nidhisync.billing.controller;

//AuthController.java
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nidhisync.billing.dto.AuthRequestDto;
import com.nidhisync.billing.dto.AuthResponseDto;
import com.nidhisync.billing.dto.RegisterRequest;
import com.nidhisync.billing.dto.UserResponseDto;
import com.nidhisync.billing.entity.Role;
import com.nidhisync.billing.entity.User;
import com.nidhisync.billing.repository.RoleRepository;
import com.nidhisync.billing.repository.UserRepository;
import com.nidhisync.billing.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthController(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequest rq) {
		User user = User.builder().username(rq.getUsername()).email(rq.getEmail())
				.password(passwordEncoder.encode(rq.getPassword())).mobileNumber(rq.getMobileNumber()) // ← set mobile
				.roles(Set.of(roleRepo.findByName("ROLE_USER").orElseThrow())).build();
		user = userRepo.save(user);

		UserResponseDto resp = UserResponseDto.builder().id(user.getId()).username(user.getUsername())
				.email(user.getEmail()).mobileNumber(user.getMobileNumber()) // ← include mobile
				.roles(user.getRoles().stream().map(Role::getName).toList()).build();
		return ResponseEntity.ok(resp);
	}

	@PostMapping("/login")
	public AuthResponseDto login(@Valid @RequestBody AuthRequestDto rq) {
		User user = userRepo.findByUsername(rq.getUsername()).orElseThrow();
		if (!passwordEncoder.matches(rq.getPassword(), user.getPassword())) {
			throw new RuntimeException("Invalid credentials");
		}
		List<String> roles = user.getRoles().stream().map(r -> r.getName()).toList();
		String token = jwtUtil.generateToken(user.getUsername(), roles);
		return new AuthResponseDto(token);
	}
}
