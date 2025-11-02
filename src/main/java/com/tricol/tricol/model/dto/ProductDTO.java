package com.tricol.tricol.model.dto;

import com.tricol.tricol.model.entity.ProductsOrder;
import com.tricol.tricol.model.entity.StockMovement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductDTO {
    private UUID uuid;
    private String name;
    private String category;
    private String description;
    private Double unitPrice;
    private Integer quantity;
    private List<StockMovement> stockMovements;
    private List<ProductsOrder> productsOrders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductDTO() {}

    public ProductDTO(UUID uuid, String name, String category, String description, Double unitPrice, Integer quantity, List<StockMovement> stockMovements, List<ProductsOrder> productsOrders) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.stockMovements = stockMovements;
        this.productsOrders = productsOrders;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<StockMovement> getStockMovements() {
        return stockMovements;
    }

    public void setStockMovements(List<StockMovement> stockMovements) {
        this.stockMovements = stockMovements;
    }

    public List<ProductsOrder> getProductsOrders() {
        return productsOrders;
    }

    public void setProductsOrders(List<ProductsOrder> productsOrders) {
        this.productsOrders = productsOrders;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
