package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerRequestDto {
  @NotBlank(message = "Name is required")
  private String name;

  @Email(message = "Must be a valid email")
  private String email;

  private String phone;
  private String address;
}
