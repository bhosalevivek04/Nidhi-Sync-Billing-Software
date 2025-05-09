package com.nidhisync.billing.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRequestDto {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@Email
	private String email;

	@NotBlank
	@Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Must be a valid phone number")
	private String mobileNumber;

	private Set<String> roles; // e.g. ["ROLE_ADMIN", "ROLE_CLEARK"]
}
