package com.nidhisync.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nidhisync.billing.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
  boolean existsByEmail(String email);
}
