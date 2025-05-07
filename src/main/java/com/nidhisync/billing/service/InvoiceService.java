package com.nidhisync.billing.service;

import com.nidhisync.billing.dto.InvoiceRequestDto;
import com.nidhisync.billing.dto.InvoiceResponseDto;

import java.util.List;

public interface InvoiceService {
  InvoiceResponseDto create(InvoiceRequestDto rq);
  List<InvoiceResponseDto> listAll();
  List<InvoiceResponseDto> listByUserId(Long userId);
  InvoiceResponseDto getById(Long id);
}
