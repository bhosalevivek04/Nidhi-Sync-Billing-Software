// src/main/java/com/nidhisync/billing/service/CustomerService.java
package com.nidhisync.billing.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nidhisync.billing.dto.CustomerRequestDto;
import com.nidhisync.billing.dto.CustomerResponseDto;
import com.nidhisync.billing.entity.Customer;
import com.nidhisync.billing.repository.CustomerRepository;

@Service
@Transactional
public class CustomerService {

  private final CustomerRepository repo;

  public CustomerService(CustomerRepository repo) {
    this.repo = repo;
  }

  public CustomerResponseDto create(CustomerRequestDto rq) {
    if (rq.getEmail() != null && repo.existsByEmail(rq.getEmail())) {
      throw new IllegalArgumentException("Email already in use");
    }
    Customer c = Customer.builder()
        .name(rq.getName())
        .email(rq.getEmail())
        .phone(rq.getPhone())
        .address(rq.getAddress())
        .build();
    c = repo.save(c);
    return toDto(c);
  }

  public List<CustomerResponseDto> listAll() {
    return repo.findAll().stream()
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  public CustomerResponseDto getById(Long id) {
    Customer c = repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    return toDto(c);
  }

  public CustomerResponseDto update(Long id, CustomerRequestDto rq) {
    Customer c = repo.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    c.setName(rq.getName());
    c.setEmail(rq.getEmail());
    c.setPhone(rq.getPhone());
    c.setAddress(rq.getAddress());
    return toDto(repo.save(c));
  }

  public void delete(Long id) {
    repo.deleteById(id);
  }

  private CustomerResponseDto toDto(Customer c) {
    return CustomerResponseDto.builder()
      .id(c.getId())
      .name(c.getName())
      .email(c.getEmail())
      .phone(c.getPhone())
      .address(c.getAddress())
      .build();
  }
}
