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
    this.itemRepo    = itemRepo;
    this.productRepo = productRepo;
  }

  @Override
  public InvoiceResponseDto create(InvoiceRequestDto rq) {
    // 1) build items and compute line totals
    List<InvoiceItem> items = rq.getItems().stream()
      .map(i -> {
        Product p = productRepo.findById(i.getProductId())
                     .orElseThrow(() -> new IllegalArgumentException("Unknown product " + i.getProductId()));
        double lineTotal = p.getPrice() * i.getQuantity();
        return InvoiceItem.builder()
                   .productId(p.getId())
                   .quantity(i.getQuantity())
                   .price(p.getPrice())
                   .lineTotal(lineTotal)
                   .build();
      })
      .collect(Collectors.toList());

    // 2) sum up invoice total
    double total = items.stream()
                        .mapToDouble(InvoiceItem::getLineTotal)
                        .sum();

    // 3) save the invoice header
    Invoice inv = Invoice.builder()
                  .userId(rq.getUserId())
                  .date(LocalDateTime.now())
                  .total(total)
                  .build();
    Invoice savedInv = invoiceRepo.save(inv);

    // 4) link items to invoice and save
    Long invoiceId = savedInv.getId();
    items.forEach(item -> item.setInvoiceId(invoiceId));
    itemRepo.saveAll(items);

    // 5) build DTO items
    List<InvoiceResponseDto.Item> dtoItems = items.stream()
      .map(it -> new InvoiceResponseDto.Item(
                   it.getProductId(),
                   it.getQuantity(),
                   it.getPrice(),
                   it.getLineTotal()))
      .collect(Collectors.toList());

    // 6) return DTO in correct constructor order: id, date, total, items, userId
    return new InvoiceResponseDto(
      savedInv.getId(),
      savedInv.getDate(),
      savedInv.getTotal(),
      dtoItems,
      savedInv.getUserId()
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

  // helper to map Invoice → InvoiceResponseDto
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
      inv.getTotal(),
      dtoItems,
      inv.getUserId()
    );
  }
}
