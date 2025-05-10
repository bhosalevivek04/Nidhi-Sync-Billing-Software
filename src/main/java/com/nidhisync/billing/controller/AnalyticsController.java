package com.nidhisync.billing.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nidhisync.billing.service.AnalyticsService;

@RestController
@RequestMapping("/api/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  /** Total Sales in a Month */
  @GetMapping("/monthly-sales")
  public ResponseEntity<Double> getMonthlySales(@RequestParam String month) {
    YearMonth ym = YearMonth.parse(month); // format: yyyy-MM
    LocalDate start = ym.atDay(1);
    LocalDate end = ym.atEndOfMonth();
    double totalSales = analyticsService.getSalesBetween(start, end);
    return ResponseEntity.ok(totalSales);
  }

  /** Daily Revenue Chart between two dates */
  @GetMapping("/revenue-chart")
  public ResponseEntity<?> revenueChart(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

    if (from.isAfter(to)) {
      return ResponseEntity.badRequest().body("Invalid date range: 'from' must be before 'to'");
    }

    return ResponseEntity.ok(analyticsService.getRevenueChart(from, to));
  }

  /** Top selling products between dates */
  @GetMapping("/top-products")
  public ResponseEntity<?> topProducts(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(defaultValue = "5") int limit) {

    if (from.isAfter(to)) {
      return ResponseEntity.badRequest().body("Invalid date range: 'from' must be before 'to'");
    }

    return ResponseEntity.ok(analyticsService.getTopSellingProducts(from, to, limit));
  }
}