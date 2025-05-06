package com.nidhisync.billing.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

@Service
public class InvoiceService {

	private final InvoiceRepository invoiceRepo;
	private final InvoiceItemRepository itemRepo;
	private final ProductRepository productRepo;

	public InvoiceService(InvoiceRepository invoiceRepo, InvoiceItemRepository itemRepo,
			ProductRepository productRepo) {
		this.invoiceRepo = invoiceRepo;
		this.itemRepo = itemRepo;
		this.productRepo = productRepo;
	}

	@Transactional
	public InvoiceResponseDto create(InvoiceRequestDto rq) {
		// 1) build items and compute total
		List<InvoiceItem> items = new ArrayList<>();
		double total = 0.0;
		for (InvoiceRequestDto.Item req : rq.getItems()) {
			Product p = productRepo.findById(req.getProductId())
					.orElseThrow(() -> new IllegalArgumentException("Unknown product " + req.getProductId()));
			double line = p.getPrice() * req.getQuantity();
			items.add(
					InvoiceItem.builder().productId(p.getId()).quantity(req.getQuantity()).price(p.getPrice()).build());
			total += line;
		}

		// 2) save invoice, capturing result in a new variable
		Invoice invToSave = Invoice.builder().userId(rq.getUserId()).date(LocalDateTime.now()).total(total).build();
		Invoice savedInv = invoiceRepo.save(invToSave);

		// 3) associate items to invoice using savedInv.getId()
		Long invoiceId = savedInv.getId();
		items.forEach(it -> it.setInvoiceId(invoiceId));
		itemRepo.saveAll(items);

		// 4) map to DTO
		List<InvoiceResponseDto.Item> dtoItems = items.stream()
				.map(it -> new InvoiceResponseDto.Item(it.getProductId(), it.getQuantity(), it.getPrice()))
				.collect(Collectors.toList());

		return new InvoiceResponseDto(savedInv.getId(), savedInv.getDate(), savedInv.getTotal(), dtoItems);
	}

	public List<InvoiceResponseDto> listAll() {
		return invoiceRepo.findAll().stream().map(inv -> {
			List<InvoiceResponseDto.Item> dtoItems = itemRepo.findByInvoiceId(inv.getId()).stream()
					.map(it -> new InvoiceResponseDto.Item(it.getProductId(), it.getQuantity(), it.getPrice()))
					.collect(Collectors.toList());
			return new InvoiceResponseDto(inv.getId(), inv.getDate(), inv.getTotal(), dtoItems);
		}).collect(Collectors.toList());
	}

	public InvoiceResponseDto getById(Long id) {
		Invoice inv = invoiceRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + id));
		List<InvoiceResponseDto.Item> dtoItems = itemRepo.findByInvoiceId(inv.getId()).stream()
				.map(it -> new InvoiceResponseDto.Item(it.getProductId(), it.getQuantity(), it.getPrice()))
				.collect(Collectors.toList());
		return new InvoiceResponseDto(inv.getId(), inv.getDate(), inv.getTotal(), dtoItems);
	}
}
