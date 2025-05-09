// src/main/java/com/nidhisync/billing/service/impl/ProductServiceImpl.java
package com.nidhisync.billing.service.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.dto.ProductRequestDto;
import com.nidhisync.billing.dto.ProductResponseDto;
import com.nidhisync.billing.entity.Category;
import com.nidhisync.billing.entity.Product;
import com.nidhisync.billing.repository.CategoryRepository;
import com.nidhisync.billing.repository.ProductRepository;
import com.nidhisync.billing.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;

	public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
	}

	@Override
	public ProductResponseDto create(ProductRequestDto rq) {
		Category cat = categoryRepo.findById(rq.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Category not found: " + rq.getCategoryId()));

		Product p = Product.builder().name(rq.getName()).description(rq.getDescription()).price(rq.getPrice())
				.stockQuantity(rq.getStockQuantity()).barcode(java.util.UUID.randomUUID().toString()).category(cat)
				.build();

		p = productRepo.save(p);
		return toDto(p);
	}

	@Override
	public List<ProductResponseDto> listAll() {
		return productRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	@Override
	public ProductResponseDto getById(Long id) {
		Product p = productRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
		return toDto(p);
	}

	@Override
	public ProductResponseDto update(Long id, ProductRequestDto rq) {
		Product p = productRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
		Category cat = categoryRepo.findById(rq.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("Category not found: " + rq.getCategoryId()));

		p.setName(rq.getName());
		p.setDescription(rq.getDescription());
		p.setPrice(rq.getPrice());
		p.setStockQuantity(rq.getStockQuantity());
		p.setCategory(cat);

		return toDto(productRepo.save(p));
	}

	@Override
	public void delete(Long id) {
		productRepo.deleteById(id);
	}

	@Override
	public ProductResponseDto getByBarcode(String barcode) {
		Product p = productRepo.findByBarcode(barcode)
				.orElseThrow(() -> new IllegalArgumentException("Product not found for barcode: " + barcode));
		return toDto(p);
	}

	@Override
	public byte[] generateBarcodeImage(String barcode) throws Exception {
		Code128Writer writer = new Code128Writer();
		BitMatrix matrix = writer.encode(barcode, BarcodeFormat.CODE_128, 300, 100);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
		return baos.toByteArray();
	}

	// null‑safe mapping into your response DTO
	private ProductResponseDto toDto(Product p) {
		Long catId = null;
		String catName = null;
		if (p.getCategory() != null) {
			catId = p.getCategory().getId();
			catName = p.getCategory().getName();
		}
		return ProductResponseDto.builder().id(p.getId()).name(p.getName()).description(p.getDescription())
				.price(p.getPrice()).stockQuantity(p.getStockQuantity()).barcode(p.getBarcode()).categoryId(catId)
				.categoryName(catName).build();
	}
}