package com.tricol.tricol.model.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<StockMovement> stockMovements;

    public Product() {};
    public Product(UUID uuid, String name, String category, String description, Double unitPrice, Integer quantity) {
        this.uuid = uuid;
        this.name = name;
        this.category = category;
        this.description = description;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
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
}
