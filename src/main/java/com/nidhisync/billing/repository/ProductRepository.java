// src/main/java/com/nidhisync/billing/repository/ProductRepository.java
package com.nidhisync.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nidhisync.billing.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
