package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {

	@NotBlank(message = "Name must not be blank")
	private String name;

	@NotNull(message = "Price is required")
	@Min(value = 0, message = "Price must be ≥ 0")
	private Double price;

	@NotNull(message = "Stock is required")
	@Min(value = 0, message = "Stock must be ≥ 0")
	private Integer stock;
}
