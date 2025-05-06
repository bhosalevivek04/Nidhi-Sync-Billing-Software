package com.nidhisync.billing.controller;
//ProductController.java
import com.nidhisync.billing.dto.ProductRequestDto;
import com.nidhisync.billing.dto.ProductResponseDto;
import com.nidhisync.billing.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService service;

  public ProductController(ProductService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDto>> list() {
    return ResponseEntity.ok(service.listAll());
  }

  @PostMapping
  public ResponseEntity<ProductResponseDto> create(@RequestBody ProductRequestDto rq) {
    return ResponseEntity.ok(service.create(rq));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponseDto> update(
      @PathVariable Long id,
      @RequestBody ProductRequestDto rq
  ) {
    return ResponseEntity.ok(service.update(id, rq));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
