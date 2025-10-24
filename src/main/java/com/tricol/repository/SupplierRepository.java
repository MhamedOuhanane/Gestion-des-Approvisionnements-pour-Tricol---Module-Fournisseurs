package com.tricol.repository;

import com.tricol.model.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Supplier findByEmail(String email);
    List<Supplier> findByEmailEndingWith(String email);
}
