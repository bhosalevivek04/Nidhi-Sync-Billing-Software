package com.nidhisync.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class UserResponseDto {
	private Long id;
	private String username;
	private String email;
	private Set<String> roles;
}
