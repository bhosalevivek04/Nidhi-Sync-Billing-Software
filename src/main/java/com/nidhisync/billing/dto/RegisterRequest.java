// src/main/java/com/nidhisync/billing/dto/RegisterRequest.java
package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

// generate no‑arg and all‑args constructors + getters/setters
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Username is required")                   // not null, not empty
  @Size(min = 3, max = 20, message = "Username must be 3–20 chars")
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Must be a valid email address")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 6, message = "Password must be at least 6 characters")
  private String password;
}