package com.tricol.tricol.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductsOrderDTO {
    private UUID productId;
    private UUID orderId;
    private String productName;
    private Integer quantity;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductsOrderDTO() {}

    public ProductsOrderDTO(UUID productId, UUID orderId, String productName, Integer quantity, Double amount) {
        this.productId = productId;
        this.orderId = orderId;
        this.productName = productName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
