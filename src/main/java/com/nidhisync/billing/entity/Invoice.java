package com.nidhisync.billing.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="invoices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice {
  @Id @GeneratedValue Long id;
  private Long userId;          // or @ManyToOne User user
  private LocalDateTime date;

  @Column(nullable = false)
  private Double subtotal;
  
  @Column(nullable = false)
  private Double taxRate;

  @Column(nullable = false)
  private Double taxAmount;

  @Column(nullable = false)
  private Double grandTotal;

}