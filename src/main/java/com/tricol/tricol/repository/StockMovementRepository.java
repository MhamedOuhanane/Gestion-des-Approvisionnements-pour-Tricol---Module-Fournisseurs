package com.tricol.tricol.repository;

import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.entity.StockMovement;
import com.tricol.tricol.model.enums.StockMovementType;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
    @Query("""
            select sm from StockMovement sm
            left join fetch sm.product p
            where (:productId is null or sm.product.uuid = :productId)
            and (:type is null or sm.type = :type)
            """)
    Page<StockMovement> findByProductAndType(
            @Param("productId") UUID productId,
            @Param("type") StockMovementType type,
            Pageable pageable
    );
}
