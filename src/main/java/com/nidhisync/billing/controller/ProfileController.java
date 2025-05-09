package com.nidhisync.billing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nidhisync.billing.dto.UserResponseDto;
import com.nidhisync.billing.service.UserService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

	private final UserService userService;

	public ProfileController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Return the current user’s profile (id, username, email, mobileNumber, roles).
	 * Only authenticated users may call this.
	 */
	@GetMapping
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<UserResponseDto> profile(Authentication auth) {
		String username = auth.getName();
		UserResponseDto dto = userService.findByUsername(username);
		return ResponseEntity.ok(dto);
	}
}
