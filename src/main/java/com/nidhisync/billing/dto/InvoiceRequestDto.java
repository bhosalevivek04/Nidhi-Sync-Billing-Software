package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class InvoiceRequestDto {

  @NotNull(message = "userId is required")
  private Long userId;

  @NotNull(message = "Tax rate is required")  // e.g., 18.0 for 18%
  private Double taxRate;

  @NotEmpty(message = "At least one item is required")
  private List<Item> items;

  @Getter @Setter
  public static class Item {
    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull @Min(value = 1, message = "Quantity must be ≥ 1")
    private Integer quantity;
  }
}