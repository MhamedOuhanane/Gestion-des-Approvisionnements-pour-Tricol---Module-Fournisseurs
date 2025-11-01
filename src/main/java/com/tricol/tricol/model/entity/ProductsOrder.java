package com.tricol.tricol.model.entity;

import com.tricol.tricol.model.entity.id.ProductsOrderId;
import jakarta.persistence.*;

@Entity
@Table(name = "products_order")
public class ProductsOrder extends Auditable {

    @EmbeddedId
    private ProductsOrderId uuid = new ProductsOrderId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    public ProductsOrder() {};

    public ProductsOrder(ProductsOrderId uuid, Product product, Order order) {
        this.uuid = uuid;
        this.product = product;
        this.order = order;
    }

    public ProductsOrderId getUuid() {
        return uuid;
    }

    public void setUuid(ProductsOrderId uuid) {
        this.uuid = uuid;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
