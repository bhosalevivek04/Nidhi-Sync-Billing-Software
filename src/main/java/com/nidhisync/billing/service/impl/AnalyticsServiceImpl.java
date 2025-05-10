package com.nidhisync.billing.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.entity.Invoice;
import com.nidhisync.billing.entity.InvoiceItem;
import com.nidhisync.billing.entity.Product;
import com.nidhisync.billing.repository.InvoiceItemRepository;
import com.nidhisync.billing.repository.InvoiceRepository;
import com.nidhisync.billing.repository.ProductRepository;
import com.nidhisync.billing.service.AnalyticsService;

@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

  private final InvoiceRepository invoiceRepo;
  private final InvoiceItemRepository invoiceItemRepo;
  private final ProductRepository productRepo;

  public AnalyticsServiceImpl(
      InvoiceRepository invoiceRepo,
      InvoiceItemRepository invoiceItemRepo,
      ProductRepository productRepo) {
    this.invoiceRepo = invoiceRepo;
    this.invoiceItemRepo = invoiceItemRepo;
    this.productRepo = productRepo;
  }

  @Override
  public double getSalesBetween(LocalDate from, LocalDate to) {
    return invoiceRepo.findAll().stream()
      .filter(i -> {
        LocalDate date = i.getDate().toLocalDate();
        return !date.isBefore(from) && !date.isAfter(to);
      })
      .mapToDouble(Invoice::getGrandTotal)
      .sum();
  }

  @Override
  public Map<LocalDate, Double> getRevenueChart(LocalDate from, LocalDate to) {
    return invoiceRepo.findAll().stream()
      .filter(i -> {
        LocalDate date = i.getDate().toLocalDate();
        return !date.isBefore(from) && !date.isAfter(to);
      })
      .collect(Collectors.groupingBy(
        i -> i.getDate().toLocalDate(),
        Collectors.summingDouble(Invoice::getGrandTotal)
      ));
  }

  @Override
  public List<Map<String, Object>> getTopSellingProducts(LocalDate from, LocalDate to, int limit) {
    return invoiceItemRepo.findAll().stream()
      .filter(item -> {
        Invoice inv = invoiceRepo.findById(item.getInvoiceId()).orElse(null);
        if (inv == null) return false;
        LocalDate date = inv.getDate().toLocalDate();
        return !date.isBefore(from) && !date.isAfter(to);
      })
      .collect(Collectors.groupingBy(
        InvoiceItem::getProductId,
        Collectors.summingInt(InvoiceItem::getQuantity)
      ))
      .entrySet().stream()
      .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
      .limit(limit)
      .map(e -> {
        Long productId = e.getKey();
        int quantity = e.getValue();
        Product product = productRepo.findById(productId).orElse(null);
        String name = (product != null) ? product.getName() : "Unknown";
        return (Map<String, Object>) (Map<?, ?>) Map.of(
            "productId", productId,
            "name", name,
            "quantitySold", quantity
        );
      })
      .collect(Collectors.toList());
  }
}