package com.nidhisync.billing.controller;
//BarcodeController.java
import com.nidhisync.billing.entity.Product;
import com.nidhisync.billing.repository.ProductRepository;
import com.nidhisync.billing.util.BarcodeUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

  private final ProductRepository productRepo;
  private final BarcodeUtil barcodeUtil;

  public BarcodeController(ProductRepository productRepo,
                           BarcodeUtil barcodeUtil) {
    this.productRepo = productRepo;
    this.barcodeUtil = barcodeUtil;
  }

  @GetMapping(value = "/{id}", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getBarcode(@PathVariable Long id) throws Exception {
    Product p = productRepo.findById(id).orElseThrow();
    byte[] png = barcodeUtil.generateBarcodePng(p.getBarcode(), 300, 100);
    return ResponseEntity.ok(png);
  }
}
