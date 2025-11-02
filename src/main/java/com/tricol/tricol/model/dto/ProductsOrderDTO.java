package com.tricol.tricol.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductsOrderDTO {
    private UUID productId;
    private UUID orderId;
    private Integer quantity;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductsOrderDTO() {}

    public ProductsOrderDTO(UUID productId, UUID orderId, Integer quantity, Double amount) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.amount = amount;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
