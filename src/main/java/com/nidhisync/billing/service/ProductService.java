package com.nidhisync.billing.service;

import com.nidhisync.billing.dto.ProductRequestDto;
import com.nidhisync.billing.dto.ProductResponseDto;
import com.nidhisync.billing.entity.Product;
import com.nidhisync.billing.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

  private final ProductRepository repo;

  public ProductService(ProductRepository repo) {
    this.repo = repo;
  }

  public List<ProductResponseDto> listAll() {
    return repo.findAll().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  public ProductResponseDto create(ProductRequestDto rq) {
    Product p = Product.builder()
      .name(rq.getName())
      .price(rq.getPrice())
      .stock(rq.getStock())
      // barcode generation stub—you’ll integrate ZXing later
      .barcode("BAR" + System.currentTimeMillis())
      .build();
    return toDto(repo.save(p));
  }

  public ProductResponseDto update(Long id, ProductRequestDto rq) {
    Product p = repo.findById(id).orElseThrow();
    p.setName(rq.getName());
    p.setPrice(rq.getPrice());
    p.setStock(rq.getStock());
    return toDto(repo.save(p));
  }

  public void delete(Long id) {
    repo.deleteById(id);
  }

  private ProductResponseDto toDto(Product p) {
    return ProductResponseDto.builder()
      .id(p.getId())
      .name(p.getName())
      .price(p.getPrice())
      .stock(p.getStock())
      .barcode(p.getBarcode())
      .build();
  }
}
