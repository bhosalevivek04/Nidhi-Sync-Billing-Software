package com.nidhisync.billing.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ProductResponseDto {
  private Long id;
  private String name;
  private Double price;
  private Integer stock;
  private String barcode;
}
