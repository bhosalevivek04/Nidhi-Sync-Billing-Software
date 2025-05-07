// src/main/java/com/nidhisync/billing/entity/Product.java
package com.nidhisync.billing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(nullable = false)
  private Double price;

  /** 
   * This tells Hibernate that the DB column is actually named “stock”
   * but in Java we call it stockQuantity.
   */
  @Column(name = "stock", nullable = false)
  private Integer stockQuantity;

  @Column(nullable = false, unique = true)
  private String barcode;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;
}
