package com.nidhisync.billing.service.impl;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.entity.Invoice;
import com.nidhisync.billing.repository.InvoiceRepository;
import com.nidhisync.billing.service.AnalyticsService;

@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

  private final InvoiceRepository invoiceRepo;

  public AnalyticsServiceImpl(InvoiceRepository invoiceRepo) {
    this.invoiceRepo = invoiceRepo;
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
}
