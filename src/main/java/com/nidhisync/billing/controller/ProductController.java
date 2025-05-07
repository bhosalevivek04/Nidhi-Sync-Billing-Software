// src/main/java/com/nidhisync/billing/controller/ProductController.java
package com.nidhisync.billing.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nidhisync.billing.dto.ProductRequestDto;
import com.nidhisync.billing.dto.ProductResponseDto;
import com.nidhisync.billing.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService svc;

  public ProductController(ProductService svc) {
    this.svc = svc;
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> list() {
    return ResponseEntity.ok(svc.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> get(@PathVariable Long id) {
    return ResponseEntity.ok(svc.getById(id));
  }

  @PostMapping
  public ResponseEntity<ProductResponseDto> create(
      @Valid @RequestBody ProductRequestDto rq) {
    return ResponseEntity.ok(svc.create(rq));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> update(
      @PathVariable Long id,
      @Valid @RequestBody ProductRequestDto rq) {
    return ResponseEntity.ok(svc.update(id, rq));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    svc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
