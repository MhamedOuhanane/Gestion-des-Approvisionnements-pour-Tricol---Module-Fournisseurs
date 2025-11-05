package com.tricol.tricol.model.entity;

import com.tricol.tricol.model.enums.StockMovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_movements")
public class StockMovement extends Auditable{
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, unique = true, nullable = false)
    private UUID uuid;

    @NotNull(message = "La date du mouvement est obligatoire")
    @Column(nullable = false)
    private LocalDateTime date;

    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Le type de mouvement est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockMovementType type;

    @NotNull(message = "Le produit est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public StockMovement() {}

    public StockMovement(LocalDateTime date, Integer quantity, StockMovementType type, Product product) {
        this.uuid = uuid;
        this.date = date;
        this.quantity = quantity;
        this.type = type;
        this.product = product;
    }

    public StockMovement(UUID uuid, LocalDateTime date, Integer quantity, StockMovementType type, Product product) {
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
}
