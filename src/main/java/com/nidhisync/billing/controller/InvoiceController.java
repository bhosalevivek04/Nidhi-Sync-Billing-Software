package com.nidhisync.billing.controller;
//InvoiceController.java

import com.nidhisync.billing.dto.InvoiceRequestDto;
import com.nidhisync.billing.dto.InvoiceResponseDto;
import com.nidhisync.billing.service.InvoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  /** Create a new invoice */
  @PostMapping
  public ResponseEntity<InvoiceResponseDto> create(
      @RequestBody InvoiceRequestDto rq) {
    InvoiceResponseDto created = invoiceService.create(rq);
    return ResponseEntity.ok(created);
  }

  /** List all invoices */
  @GetMapping
  public ResponseEntity<List<InvoiceResponseDto>> list() {
    List<InvoiceResponseDto> all = invoiceService.listAll();
    return ResponseEntity.ok(all);
  }

  /** Get one invoice by ID */
  @GetMapping("/{id}")
  public ResponseEntity<InvoiceResponseDto> get(
      @PathVariable Long id) {
    InvoiceResponseDto dto = invoiceService.getById(id);
    return ResponseEntity.ok(dto);
  }
}