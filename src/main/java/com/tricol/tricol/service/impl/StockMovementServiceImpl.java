package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.entity.StockMovement;
import com.tricol.tricol.model.enums.StockMovementType;
import com.tricol.tricol.model.mapper.StockMovementMapper;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.repository.StockMovementRepository;
import com.tricol.tricol.service.interfaces.ProductService;
import com.tricol.tricol.service.interfaces.StockMovementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class StockMovementServiceImpl implements StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final StockMovementMapper stockMovementMapper;
    private final ProductRepository productRepository;

    public StockMovementServiceImpl(StockMovementRepository stockMovementRepository, StockMovementMapper stockMovementMapper, ProductRepository productRepository) {
        this.stockMovementRepository = stockMovementRepository;
        this.stockMovementMapper = stockMovementMapper;
        this.productRepository = productRepository;
    }

    @Override
    public void create(Product product, Integer quantity, StockMovementType type) {
        StockMovement stockMovement = new StockMovement(LocalDateTime.now(), quantity, type, product);
        stockMovementRepository.save(stockMovement);
    }

    @Override
    public Map<String, Object> findAll(Map<String, Object> filter) {
        if (filter.isEmpty()) filter = Map.of("page", 0, "size", 5);
        Pageable pageable = PageRequest.of((int) filter.get("page"), (int) filter.get("size"), Sort.by("date").descending());
        UUID productId = (UUID) filter.getOrDefault("productId", null);
        StockMovementType type = !Objects.equals((String) filter.get("type"), "")
                ? StockMovementType.valueOf((String) filter.get("type"))
                : null;

        Product product = null;
        if (productId != null)
             product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException("Aucun produit trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        
        Page<StockMovement> stockMovements = stockMovementRepository.findByProductAndType(productId, type, pageable);

        String message = stockMovements.isEmpty() ?
                product != null ?
                        "Aucune mouvement de stock n'existe pour ce produit":
                        "Aucune mouvement de stock n'existe dans le système":
                product != null ?
                        "Les mouvements de stock du produit '" + product.getName() + "' trouvées avec succès":
                        "Les mouvements de stock trouvées avec succès";
        Object data = stockMovements.isEmpty() ?
                List.of():
                stockMovements.stream()
                        .map(stockMovementMapper::toDto)
                        .toList();

        return Map.of(
                "message", message,
                "status", 200,
                "data", data,
                "pagination", Map.of(
                        "page", stockMovements.getNumber(),
                        "size", stockMovements.getSize(),
                        "totalElements", stockMovements.getTotalElements(),
                        "totalPages", stockMovements.getTotalPages(),
                        "isFirst", stockMovements.isFirst(),
                        "isLast", stockMovements.isLast()
                )
        );
    }
}
