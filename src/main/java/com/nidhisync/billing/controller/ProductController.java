// src/main/java/com/nidhisync/billing/controller/ProductController.java
package com.nidhisync.billing.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nidhisync.billing.dto.ProductRequestDto;
import com.nidhisync.billing.dto.ProductResponseDto;
import com.nidhisync.billing.service.ProductService;

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

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
    return ResponseEntity.ok(service.getById(id));
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

  // ─── New barcode lookup ───────────────────────────────────────────────────────

  @GetMapping("/barcode/{code}")
  public ResponseEntity<ProductResponseDto> getByBarcode(@PathVariable("code") String code) {
    return ResponseEntity.ok(service.getByBarcode(code));
  }

  @GetMapping(
    value = "/barcode/{code}/image",
    produces = MediaType.IMAGE_PNG_VALUE
  )
  public ResponseEntity<byte[]> barcodeImage(@PathVariable("code") String code) throws Exception {
    byte[] img = service.generateBarcodeImage(code);
    return ResponseEntity.ok(img);
  }
}
