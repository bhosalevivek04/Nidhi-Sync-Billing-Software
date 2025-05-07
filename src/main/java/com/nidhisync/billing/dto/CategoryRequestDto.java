// src/main/java/com/nidhisync/billing/dto/CategoryRequestDto.java
package com.nidhisync.billing.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Add Lombok annotations to generate getter/setter, constructors, builder if you like
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CategoryRequestDto {
  
  @NotBlank(message = "Category name is required")
  private String name;
}
