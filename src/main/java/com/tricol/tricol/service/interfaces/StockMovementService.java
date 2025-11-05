package com.tricol.tricol.service.interfaces;

import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.enums.StockMovementType;

import java.util.Map;

public interface StockMovementService {
    void create(Product product, Integer quantity, StockMovementType type);
    Map<String, Object> findAll(Map<String, Object> filter);
}
