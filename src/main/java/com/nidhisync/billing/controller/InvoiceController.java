package com.nidhisync.billing.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nidhisync.billing.dto.InvoiceRequestDto;
import com.nidhisync.billing.dto.InvoiceResponseDto;
import com.nidhisync.billing.service.InvoiceService;
import com.nidhisync.billing.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;
  private final UserService userService;    // to lookup current user’s id

  public InvoiceController(InvoiceService invoiceService,
                           UserService userService) {
    this.invoiceService = invoiceService;
    this.userService = userService;
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('CLERK','ADMIN')")
  public ResponseEntity<InvoiceResponseDto> create(@RequestBody @Valid InvoiceRequestDto rq) {
    return ResponseEntity.ok(invoiceService.create(rq));
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('USER','CLERK','ADMIN')")
  public ResponseEntity<List<InvoiceResponseDto>> list(
      @RequestParam(required = false) Long userId,
      Authentication auth) {

    boolean isUserOnly = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
     && auth.getAuthorities().stream()
        .noneMatch(a -> a.getAuthority().equals("ROLE_CLERK") || a.getAuthority().equals("ROLE_ADMIN"));

    if (isUserOnly) {
      // ignore caller‑supplied userId; only their own
      Long myUserId = userService.findByUsername(auth.getName()).getId();
      return ResponseEntity.ok(invoiceService.listByUserId(myUserId));
    }

    // clerk/admin: if they passed a userId, filter, otherwise return all
    if (userId != null) {
      return ResponseEntity.ok(invoiceService.listByUserId(userId));
    }
    return ResponseEntity.ok(invoiceService.listAll());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('USER','CLERK','ADMIN')")
  public ResponseEntity<InvoiceResponseDto> get(
      @PathVariable Long id,
      Authentication auth) throws AccessDeniedException {

    InvoiceResponseDto dto = invoiceService.getById(id);

    boolean isUserOnly = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
     && auth.getAuthorities().stream()
        .noneMatch(a -> a.getAuthority().equals("ROLE_CLERK") || a.getAuthority().equals("ROLE_ADMIN"));

    if (isUserOnly && !dto.getUserId().equals(
           userService.findByUsername(auth.getName()).getId())) {
      throw new AccessDeniedException("Users may only view their own invoices");
    }

    return ResponseEntity.ok(dto);
  }
}