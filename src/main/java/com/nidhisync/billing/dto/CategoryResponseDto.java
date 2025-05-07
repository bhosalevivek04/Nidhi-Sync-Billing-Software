// src/main/java/com/nidhisync/billing/dto/CategoryResponseDto.java
package com.nidhisync.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder                  // ← this generates the static builder() method
public class CategoryResponseDto {
  private Long id;
  private String name;
}
