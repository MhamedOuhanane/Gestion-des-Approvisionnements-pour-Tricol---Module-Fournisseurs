package com.tricol.tricol.repository;

import com.tricol.tricol.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @EntityGraph(attributePaths = {"supplier", "productsOrders", "productsOrders.product"})
    @Query("""
            select o from Order o
            left join o.supplier s
            left join o.productsOrders po
            left join po.product p
            where (:supplierContact is null or lower(s.contact) like lower(concat('%', :supplierContact, '%')))
            and (:productName is null or lower(p.name) like lower(concat('%', :productName, '%')))
            and (:status is null or lower(o.status) = lower(:status))
            and (:supplierId is null or s.uuid = :supplierId)
            """)
    Page<Order> findAllWithFilters(
            @Param("supplierContact") String supplierContact,
            @Param("productName") String productName,
            @Param("status") String status,
            @Param("supplierId") UUID supplierId,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"supplier", "productsOrders", "productsOrders.product"})
    Optional<Order> findDetailedById(UUID uuid);
}
