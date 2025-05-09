package com.nidhisync.billing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRolesUpdateDto {
	@NotNull(message = "roles must be provided")
	private Set<String> roles;
}
