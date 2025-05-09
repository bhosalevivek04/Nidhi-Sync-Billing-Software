package com.nidhisync.billing.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
public class UserResponseDto {
  private Long id;
  private String username;
  private String email;
  private String mobileNumber;        // ← new
  private List<String> roles;
}
