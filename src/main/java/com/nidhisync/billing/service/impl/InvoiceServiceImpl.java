package com.nidhisync.billing.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.dto.InvoiceRequestDto;
import com.nidhisync.billing.dto.InvoiceResponseDto;
import com.nidhisync.billing.entity.Invoice;
import com.nidhisync.billing.entity.InvoiceItem;
import com.nidhisync.billing.entity.Product;
import com.nidhisync.billing.repository.InvoiceItemRepository;
import com.nidhisync.billing.repository.InvoiceRepository;
import com.nidhisync.billing.repository.ProductRepository;
import com.nidhisync.billing.service.InvoiceService;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

  private final InvoiceRepository invoiceRepo;
  private final InvoiceItemRepository itemRepo;
  private final ProductRepository productRepo;

  public InvoiceServiceImpl(InvoiceRepository invoiceRepo,
                            InvoiceItemRepository itemRepo,
                            ProductRepository productRepo) {
    this.invoiceRepo = invoiceRepo;
    this.itemRepo = itemRepo;
    this.productRepo = productRepo;
  }

  @Override
  public InvoiceResponseDto create(InvoiceRequestDto rq) {
    // 1. Build invoice items and validate stock
    List<InvoiceItem> items = rq.getItems().stream().map(i -> {
      Product p = productRepo.findById(i.getProductId())
        .orElseThrow(() -> new IllegalArgumentException("Unknown product " + i.getProductId()));

      if (p.getStockQuantity() < i.getQuantity()) {
        throw new IllegalArgumentException("Insufficient stock for product '" + p.getName() + "'");
      }

      double lineTotal = p.getPrice() * i.getQuantity();
      return InvoiceItem.builder()
        .productId(p.getId())
        .quantity(i.getQuantity())
        .price(p.getPrice())
        .lineTotal(lineTotal)
        .build();
    }).collect(Collectors.toList());

    // 2. Update stock quantities
    items.forEach(it -> {
      Product p = productRepo.findById(it.getProductId()).get();
      p.setStockQuantity(p.getStockQuantity() - it.getQuantity());
      productRepo.save(p);
    });

    // 3. Compute financials
    double subtotal = items.stream().mapToDouble(InvoiceItem::getLineTotal).sum();
    double taxRate = rq.getTaxRate(); // assumed to be in percentage, e.g., 18.0
    double taxAmount = (subtotal * taxRate) / 100.0;
    double grandTotal = subtotal + taxAmount;

    // 4. Save invoice header
    Invoice invoice = Invoice.builder()
      .userId(rq.getUserId())
      .date(LocalDateTime.now())
      .subtotal(subtotal)
      .taxRate(taxRate)
      .taxAmount(taxAmount)
      .grandTotal(grandTotal)
      .build();
    Invoice saved = invoiceRepo.save(invoice);

    // 5. Associate items and save
    items.forEach(item -> item.setInvoiceId(saved.getId()));
    itemRepo.saveAll(items);

    // 6. Build response DTO
    List<InvoiceResponseDto.Item> dtoItems = items.stream().map(it ->
      new InvoiceResponseDto.Item(
        it.getProductId(),
        it.getQuantity(),
        it.getPrice(),
        it.getLineTotal())
    ).collect(Collectors.toList());

    return new InvoiceResponseDto(
      saved.getId(),
      saved.getDate(),
      saved.getSubtotal(),
      dtoItems,
      saved.getUserId(),
      saved.getTaxRate(),
      saved.getTaxAmount(),
      saved.getGrandTotal()
    );
  }

  @Override
  public List<InvoiceResponseDto> listAll() {
    return invoiceRepo.findAll().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  @Override
  public List<InvoiceResponseDto> listByUserId(Long userId) {
    return invoiceRepo.findAll().stream()
      .filter(inv -> inv.getUserId().equals(userId))
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  @Override
  public InvoiceResponseDto getById(Long id) {
    Invoice inv = invoiceRepo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
    return toDto(inv);
  }

  // Helper
  private InvoiceResponseDto toDto(Invoice inv) {
    List<InvoiceResponseDto.Item> dtoItems = itemRepo.findByInvoiceId(inv.getId()).stream()
      .map(it -> new InvoiceResponseDto.Item(
        it.getProductId(),
        it.getQuantity(),
        it.getPrice(),
        it.getLineTotal()))
      .collect(Collectors.toList());

    return new InvoiceResponseDto(
      inv.getId(),
      inv.getDate(),
      inv.getSubtotal(),
      dtoItems,
      inv.getUserId(),
      inv.getTaxRate(),
      inv.getTaxAmount(),
      inv.getGrandTotal()
    );
  }
}