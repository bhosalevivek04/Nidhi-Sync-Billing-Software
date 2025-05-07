// src/main/java/com/nidhisync/billing/dto/ProductResponseDto.java
package com.nidhisync.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResponseDto {
  private Long id;
  private String name;
  private String description;
  private Double price;
  private Integer stockQuantity;
  private String barcode;
  private Long categoryId;
  private String categoryName;
}
