// src/main/java/com/nidhisync/billing/dto/InvoiceResponseDto.java
package com.nidhisync.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class InvoiceResponseDto {
  private Long id;
  private LocalDateTime date;
  private double total;
  private List<Item> items;
  private Long userId;              // ← new field

  @Getter @Setter @AllArgsConstructor
  public static class Item {
    private Long productId;
    private int quantity;
    private double price;
    private double lineTotal;
  }
}