package com.nidhisync.billing.exception;

import java.util.stream.Collectors;

import com.nidhisync.billing.dto.ErrorResponse;            // ← correct import
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
    var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
      .map(fe -> new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()))
      .collect(Collectors.toList());
    return new ErrorResponse("Validation failed", fieldErrors);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBadArgs(IllegalArgumentException ex) {
    return new ErrorResponse(ex.getMessage(), null);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleAll(Exception ex) {
    return new ErrorResponse("Unexpected error: " + ex.getMessage(), null);
  }
}
