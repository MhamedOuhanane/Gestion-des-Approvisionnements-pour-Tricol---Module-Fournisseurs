package com.tricol.tricol.service.interfaces;

import com.tricol.tricol.model.entity.StockMovement;

import java.util.Map;

public interface StockMovementService {
    void create(StockMovement stockMovement);
    Map<String, Object> findAll(Map<String, Object> filter);
}
