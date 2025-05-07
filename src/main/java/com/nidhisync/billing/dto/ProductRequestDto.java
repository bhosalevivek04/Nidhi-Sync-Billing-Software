// src/main/java/com/nidhisync/billing/dto/ProductRequestDto.java
package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductRequestDto {
  @NotBlank(message = "Name is required")
  private String name;

  private String description;

  @NotNull @Min(value = 0, message = "Price must be ≥ 0")
  private Double price;

  @NotNull @Min(value = 0, message = "Stock must be ≥ 0")
  private Integer stockQuantity;

  @NotNull(message = "Category ID is required")
  private Long categoryId;
}
