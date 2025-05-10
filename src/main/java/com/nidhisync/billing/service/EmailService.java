package com.nidhisync.billing.service;

public interface EmailService {
    void sendInvoice(String to, byte[] pdfData, String invoiceId);
}
