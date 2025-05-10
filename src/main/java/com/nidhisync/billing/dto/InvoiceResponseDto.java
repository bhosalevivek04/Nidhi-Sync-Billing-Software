package com.nidhisync.billing.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto {
  private Long id;
  private LocalDateTime date;
  private Double total;
  private List<Item> items;
  private Long userId;

  private Double taxRate;      // e.g., 18.0
  private Double taxAmount;    // computed = total * taxRate/100
  private Double grandTotal;   // computed = total + taxAmount

  @Getter @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Item {
    private Long productId;
    private Integer quantity;
    private Double price;
    private Double lineTotal;
  }
}