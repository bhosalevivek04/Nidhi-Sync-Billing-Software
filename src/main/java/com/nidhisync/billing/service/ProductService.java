// src/main/java/com/nidhisync/billing/service/ProductService.java
package com.nidhisync.billing.service;

import java.util.List;

import com.nidhisync.billing.dto.ProductRequestDto;
import com.nidhisync.billing.dto.ProductResponseDto;

public interface ProductService {
  ProductResponseDto create(ProductRequestDto rq);
  List<ProductResponseDto> listAll();
  ProductResponseDto getById(Long id);
  ProductResponseDto update(Long id, ProductRequestDto rq);
  void delete(Long id);
  
  ProductResponseDto getByBarcode(String barcode);
  byte[] generateBarcodeImage(String barcode) throws Exception;
}
