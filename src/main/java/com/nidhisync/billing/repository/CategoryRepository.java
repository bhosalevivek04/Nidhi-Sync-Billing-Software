package com.nidhisync.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nidhisync.billing.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
