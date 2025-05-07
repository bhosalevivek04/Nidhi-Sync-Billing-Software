// src/main/java/com/nidhisync/billing/controller/CategoryController.java
package com.nidhisync.billing.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nidhisync.billing.dto.CategoryRequestDto;
import com.nidhisync.billing.dto.CategoryResponseDto;
import com.nidhisync.billing.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService svc;

  public CategoryController(CategoryService svc) {
    this.svc = svc;
  }

  @GetMapping
  public ResponseEntity<List<CategoryResponseDto>> list() {
    return ResponseEntity.ok(svc.listAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> get(@PathVariable Long id) {
    return ResponseEntity.ok(svc.getById(id));
  }

  @PostMapping
  public ResponseEntity<CategoryResponseDto> create(
      @Valid @RequestBody CategoryRequestDto rq) {
    return ResponseEntity.ok(svc.create(rq));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponseDto> update(
      @PathVariable Long id,
      @Valid @RequestBody CategoryRequestDto rq) {
    return ResponseEntity.ok(svc.update(id, rq));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    svc.delete(id);
    return ResponseEntity.noContent().build();
  }
}
