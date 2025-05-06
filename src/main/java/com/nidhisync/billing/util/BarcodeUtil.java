package com.nidhisync.billing.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.file.FileSystems;

@Component
public class BarcodeUtil {

  /** Generate a CODE_128 barcode PNG for the given text. */
  public byte[] generateBarcodePng(String text, int width, int height) throws Exception {
    BitMatrix matrix = new MultiFormatWriter()
        .encode(text, BarcodeFormat.CODE_128, width, height);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
    return baos.toByteArray();
  }
}
