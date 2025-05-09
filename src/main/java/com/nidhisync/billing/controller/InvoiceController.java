package com.nidhisync.billing.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.nidhisync.billing.dto.InvoiceRequestDto;
import com.nidhisync.billing.dto.InvoiceResponseDto;
import com.nidhisync.billing.service.InvoiceService;
import com.nidhisync.billing.service.UserService;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;
  private final UserService userService;

  public InvoiceController(InvoiceService invoiceService,
                           UserService userService) {
    this.invoiceService = invoiceService;
    this.userService    = userService;
  }

  /** Clerks and Admins may create invoices */
  @PostMapping
  @PreAuthorize("hasAnyRole('CLERK','ADMIN')")
  public ResponseEntity<InvoiceResponseDto> create(
      @RequestBody @Valid InvoiceRequestDto rq) {
    InvoiceResponseDto created = invoiceService.create(rq);
    return ResponseEntity.ok(created);
  }

  /** 
   * List invoices:
   *  – USER sees only their own 
   *  – CLERK/ADMIN may see all or filter by userId 
   */
  @GetMapping
  @PreAuthorize("hasAnyRole('USER','CLERK','ADMIN')")
  public ResponseEntity<List<InvoiceResponseDto>> list(
      @RequestParam(required = false) Long userId,
      Authentication auth) {

    boolean isUserOnly = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
     && auth.getAuthorities().stream()
        .noneMatch(a -> a.getAuthority().equals("ROLE_CLERK") 
                     || a.getAuthority().equals("ROLE_ADMIN"));

    if (isUserOnly) {
      Long myUserId = userService.findByUsername(auth.getName()).getId();
      return ResponseEntity.ok(invoiceService.listByUserId(myUserId));
    }

    // CLERK or ADMIN
    if (userId != null) {
      return ResponseEntity.ok(invoiceService.listByUserId(userId));
    }
    return ResponseEntity.ok(invoiceService.listAll());
  }

  /**
   * Get a single invoice:
   *  – USER may only fetch their own
   *  – CLERK/ADMIN may fetch any
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('USER','CLERK','ADMIN')")
  public ResponseEntity<InvoiceResponseDto> get(
      @PathVariable Long id,
      Authentication auth) {

    InvoiceResponseDto dto = invoiceService.getById(id);

    boolean isUserOnly = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
     && auth.getAuthorities().stream()
        .noneMatch(a -> a.getAuthority().equals("ROLE_CLERK") 
                     || a.getAuthority().equals("ROLE_ADMIN"));

    if (isUserOnly) {
      Long myUserId = userService.findByUsername(auth.getName()).getId();
      if (!dto.getUserId().equals(myUserId)) {
        throw new AccessDeniedException("Users may only view their own invoices");
      }
    }

    return ResponseEntity.ok(dto);
  }
}