package com.nidhisync.billing.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {
	double getSalesBetween(LocalDate from, LocalDate to);

	Map<LocalDate, Double> getRevenueChart(LocalDate from, LocalDate to);

	List<Map<String, Object>> getTopSellingProducts(LocalDate from, LocalDate to, int limit);

}
