package com.nidhisync.billing.service;


import java.util.List;
import com.nidhisync.billing.dto.CategoryRequestDto;
import com.nidhisync.billing.dto.CategoryResponseDto;

public interface CategoryService {
CategoryResponseDto create(CategoryRequestDto rq);
List<CategoryResponseDto> listAll();
CategoryResponseDto getById(Long id);
CategoryResponseDto update(Long id, CategoryRequestDto rq);
void delete(Long id);
}
