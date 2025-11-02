package com.tricol.tricol.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", nullable = false, updatable = false, unique = true)
    private UUID uuid;

    @NotBlank(message = "le nom du produit est obligatoire")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "La catégorie du produit est obligatoire")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "la description du produit est obligatoire")
    @Column(nullable = false)
    private String description;

    @NotNull(message = "le prix du produit est obligatoire")
    @Positive(message = "le prix doit être positif")
    @Column(nullable = false)
    private Double unitPrice;

    @Positive(message = "la quantité doit être positive")
    @Column(nullable = false)
    private Integer quantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.DETACH)
    private List<StockMovement> stockMovements = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.DETACH)
    private List<ProductsOrder> productsOrders = new ArrayList<>();

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
}
