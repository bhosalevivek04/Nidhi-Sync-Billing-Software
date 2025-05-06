package com.nidhisync.billing.dto;

import java.util.List;
import lombok.*;

// top‑level error response
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ErrorResponse {
  private String message;
  private List<FieldError> fieldErrors;

  // nested DTO for field errors
  @Getter @Setter @NoArgsConstructor @AllArgsConstructor
  public static class FieldError {
    private String field;
    private String error;
  }
}
