package com.nidhisync.billing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

// generate no‑arg and all‑args constructors + getters/setters
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AuthRequestDto {

  @NotBlank(message = "Username is required")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;
}
