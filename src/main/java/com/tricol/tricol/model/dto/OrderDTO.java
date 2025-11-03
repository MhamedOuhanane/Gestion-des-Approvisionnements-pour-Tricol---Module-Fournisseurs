package com.tricol.tricol.model.dto;

import com.tricol.tricol.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDTO {
    private UUID uuid;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Double totalAmount;

    @NotNull(message = "Le fournisseur est obligatoire")
    private UUID supplierId;


    private String supplierContact;

    @NotNull(message = "La liste des produits est obligatoire")
    private List<ProductsOrderDTO>  productsOrders;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDTO() {}

    public OrderDTO(UUID uuid, LocalDateTime orderDate, OrderStatus status, Double totalAmount, UUID supplierId, String supplierContact, List<ProductsOrderDTO> productsOrders, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.uuid = uuid;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.supplierId = supplierId;
        this.supplierContact = supplierContact;
        this.productsOrders = productsOrders;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        this.supplierContact = supplierContact;
    }

    public List<ProductsOrderDTO> getProductsOrders() {
        return productsOrders;
    }

    public void setProductsOrders(List<ProductsOrderDTO> productsOrders) {
        this.productsOrders = productsOrders;
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
