// src/main/java/com/nidhisync/billing/service/impl/CategoryServiceImpl.java
package com.nidhisync.billing.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.dto.CategoryRequestDto;
import com.nidhisync.billing.dto.CategoryResponseDto;
import com.nidhisync.billing.entity.Category;
import com.nidhisync.billing.repository.CategoryRepository;
import com.nidhisync.billing.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository repo;

  public CategoryServiceImpl(CategoryRepository repo) {
    this.repo = repo;
  }

  @Override
  public CategoryResponseDto create(CategoryRequestDto rq) {
    Category c = Category.builder()
        .name(rq.getName())
        .build();
    c = repo.save(c);
    return toDto(c);
  }

  @Override
  public List<CategoryResponseDto> listAll() {
    return repo.findAll().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  @Override
  public CategoryResponseDto getById(Long id) {
    Category c = repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    return toDto(c);
  }

  @Override
  public CategoryResponseDto update(Long id, CategoryRequestDto rq) {
    Category c = repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    c.setName(rq.getName());
    return toDto(repo.save(c));
  }

  @Override
  public void delete(Long id) {
    repo.deleteById(id);
  }

  private CategoryResponseDto toDto(Category c) {
    return CategoryResponseDto.builder()
      .id(c.getId())
      .name(c.getName())
      .build();
  }
}
