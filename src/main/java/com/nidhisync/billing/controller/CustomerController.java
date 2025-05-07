// src/main/java/com/nidhisync/billing/controller/CustomerController.java
package com.nidhisync.billing.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nidhisync.billing.dto.CustomerRequestDto;
import com.nidhisync.billing.dto.CustomerResponseDto;
import com.nidhisync.billing.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

  private final CustomerService svc;

  public CustomerController(CustomerService svc) {
    this.svc = svc;
  }

  @GetMapping
  public ResponseEntity<List<CustomerResponseDto>> list() {
    return ResponseEntity.ok(svc.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CustomerResponseDto> get(@PathVariable Long id) {
    return ResponseEntity.ok(svc.getById(id));
  }

  @PostMapping
  public ResponseEntity<CustomerResponseDto> create(
      @Valid @RequestBody CustomerRequestDto rq) {
    return ResponseEntity.ok(svc.create(rq));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CustomerResponseDto> update(
      @PathVariable Long id,
      @Valid @RequestBody CustomerRequestDto rq) {
    return ResponseEntity.ok(svc.update(id, rq));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    svc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
