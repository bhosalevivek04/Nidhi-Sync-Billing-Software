package com.nidhisync.billing.service.impl;

import java.io.ByteArrayOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;
import com.nidhisync.billing.dto.InvoiceResponseDto;
import com.nidhisync.billing.service.PdfService;

@Service
public class PdfServiceImpl implements PdfService {

  @Override
  public byte[] generateInvoicePdf(InvoiceResponseDto inv) {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      Document doc = new Document();
      PdfWriter.getInstance(doc, baos);
      doc.open();

      // header
      doc.add(new Paragraph("NidhiSync Invoice", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20)));
      doc.add(new Paragraph("Invoice # " + inv.getId()));
      doc.add(new Paragraph("Date: " + inv.getDate().toString()));
      doc.add(Chunk.NEWLINE);

      // build table: columns: productId, qty, price, lineTotal
      PdfPTable table = new PdfPTable(4);
      table.setWidths(new float[]{2,1,1,1});
      table.addCell("Product ID");
      table.addCell("Qty");
      table.addCell("Unit Price");
      table.addCell("Line Total");

      inv.getItems().forEach(i -> {
        table.addCell(String.valueOf(i.getProductId()));
        table.addCell(String.valueOf(i.getQuantity()));
        table.addCell(String.format("%.2f", i.getPrice()));
        table.addCell(String.format("%.2f", i.getLineTotal()));
      });

      doc.add(table);
      doc.add(Chunk.NEWLINE);

      // total
      doc.add(new Paragraph("TOTAL: " + String.format("%.2f", inv.getTotal()),
                           FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));

      doc.close();
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Failed to generate PDF", e);
    }
  }
}
