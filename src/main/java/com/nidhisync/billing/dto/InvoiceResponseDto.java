package com.nidhisync.billing.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceResponseDto {
	private Long id;
	private LocalDateTime date;
	private Double total;
	private List<Item> items;

	@Data
	@AllArgsConstructor
	public static class Item {
		Long productId;
		Integer quantity;
		Double price;
	}
}