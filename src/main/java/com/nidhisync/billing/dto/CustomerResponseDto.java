package com.nidhisync.billing.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CustomerResponseDto {
  private Long id;
  private String name;
  private String email;
  private String phone;
  private String address;
}