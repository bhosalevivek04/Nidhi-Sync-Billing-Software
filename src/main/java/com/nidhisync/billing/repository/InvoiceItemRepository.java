package com.nidhisync.billing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nidhisync.billing.entity.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem,Long> {
  // custom finder for service.listAll()
  List<InvoiceItem> findByInvoiceId(Long invoiceId);
}