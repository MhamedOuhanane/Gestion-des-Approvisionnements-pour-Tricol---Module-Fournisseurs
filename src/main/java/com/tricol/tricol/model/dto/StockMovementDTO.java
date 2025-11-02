package com.tricol.tricol.model.dto;

import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.enums.StockMovementType;

import java.time.LocalDateTime;
import java.util.UUID;

public class StockMovementDTO {
    private UUID uuid;
    private LocalDateTime date;
    private Integer quantity;
    private StockMovementType type;
    private Product product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockMovementDTO() {}

    public StockMovementDTO(UUID uuid, LocalDateTime date, Integer quantity, StockMovementType type, Product product) {
        this.uuid = uuid;
        this.date = date;
        this.quantity = quantity;
        this.type = type;
        this.product = product;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public StockMovementType getType() {
        return type;
    }

    public void setType(StockMovementType type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
