package com.tricol.tricol.repository;

import com.tricol.tricol.model.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Page<Supplier> findByCityContainingAndContactContainingAndCompanyContaining(String city, String contact, String company, Pageable pageable);
    Page<Supplier> findByCityContainingAndContactContaining(String city, String contact, Pageable pageable);
    Page<Supplier> findByCityContainingAndCompanyContaining(String city, String company, Pageable pageable);
    Page<Supplier> findByContactContainingAndCompanyContaining(String contact, String company, Pageable pageable);
    Page<Supplier> findByCityContaining(String city, Pageable pageable);
    Page<Supplier> findByContactContaining(String contact, Pageable pageable);
    Page<Supplier> findByCompanyContaining(String company, Pageable pageable);
    Page<Supplier> findSuppliers(Pageable pageable);
    Optional<Supplier> findByEmail(String email);
    Optional<Supplier> findByICE(String ice);
}