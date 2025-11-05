package com.tricol.tricol.service.impl;

import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.entity.StockMovement;
import com.tricol.tricol.model.enums.OrderStatus;
import com.tricol.tricol.model.enums.StockMovementType;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.repository.StockMovementRepository;
import com.tricol.tricol.service.interfaces.StockMovementService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class StockMovementServiceImpl implements StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;

    public StockMovementServiceImpl(StockMovementRepository stockMovementRepository, ProductRepository productRepository) {
        this.stockMovementRepository = stockMovementRepository;
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
        return Map.of();
    }
}
