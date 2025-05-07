package com.nidhisync.billing.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    private Set<String> roles; // e.g. ["ROLE_ADMIN", "ROLE_CLEARK"]
}
