package com.nidhisync.billing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nidhisync.billing.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  // no methods needed—JpaRepository gives us save, findAll, findById, delete, etc. :contentReference[oaicite:0]{index=0}
	Optional<Role> findByName(String name);
}