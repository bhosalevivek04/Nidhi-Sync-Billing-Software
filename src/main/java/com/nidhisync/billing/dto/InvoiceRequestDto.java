package com.nidhisync.billing.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class InvoiceRequestDto {

  @NotNull(message = "userId is required")
  private Long userId;

  @NotEmpty(message = "At least one item is required")
  private List<Item> items;

  @Getter @Setter
  @NoArgsConstructor @AllArgsConstructor
  public static class Item {
    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull
    @Min(value = 1, message = "Quantity must be ≥ 1")
    private Integer quantity;
  }
}
