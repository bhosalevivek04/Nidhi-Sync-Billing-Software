package com.nidhisync.billing.service;

import com.nidhisync.billing.dto.InvoiceResponseDto;

public interface PdfService {
  byte[] generateInvoicePdf(InvoiceResponseDto invoice);
}
