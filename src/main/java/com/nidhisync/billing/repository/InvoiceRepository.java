package com.nidhisync.billing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nidhisync.billing.entity.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice,Long> {
	List<Invoice> findByUserId(Long userId);

}