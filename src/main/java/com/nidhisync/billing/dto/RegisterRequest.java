// src/main/java/com/nidhisync/billing/dto/RegisterRequest.java
package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
@NotBlank(message = "Username is required")
private String username;

@NotBlank(message = "Email is required")
@Email(message = "Must be a valid email")
private String email;

@NotBlank(message = "Password is required")
@Size(min = 6, message = "Password must be at least 6 characters")
private String password;

@NotBlank(message = "Mobile number is required")
@Pattern(regexp="^\\+?[0-9]{7,15}$", message="Must be a valid phone number")
private String mobileNumber;        // ← new
}
