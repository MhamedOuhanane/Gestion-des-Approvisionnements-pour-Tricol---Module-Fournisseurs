package com.tricol.tricol.repository;

import com.tricol.tricol.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Page<Product> findByNameContainingAndCategoryContaining(Pageable pageable, String name, String category);
    Optional<Product> findByNameAndUnitPrice(String name, Double unitPrice);
    List<Product> findByName(String name);
}
