package com.tricol.tricol.model.dto;

import com.tricol.tricol.model.entity.ProductsOrder;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDTO {
    private UUID uuid;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Double totalAmount;
    private Supplier supplier;
    private List<ProductsOrder>  productsOrders;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDTO() {}

    public OrderDTO(UUID uuid, LocalDateTime orderDate, OrderStatus status, Double totalAmount, Supplier supplier, List<ProductsOrder> productsOrders) {
        this.uuid = uuid;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.supplier = supplier;
        this.productsOrders = productsOrders;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
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
