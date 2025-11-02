package com.tricol.tricol.model.dto;

import com.tricol.tricol.model.enums.StockMovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public class StockMovementDTO {
    private UUID uuid;
    private LocalDateTime date;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Integer quantity;

    @NotNull(message = "Le type de mouvement est obligatoire")
    private StockMovementType type;

    @NotNull(message = "Le produit est obligatoire")
    private UUID productId;

    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StockMovementDTO() {}

    public StockMovementDTO(UUID uuid, LocalDateTime date, Integer quantity, StockMovementType type, UUID productId, String productName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.date = date;
        this.quantity = quantity;
        this.type = type;
        this.productId = productId;
        this.productName = productName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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
