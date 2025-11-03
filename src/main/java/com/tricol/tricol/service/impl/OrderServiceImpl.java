package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.ProductsOrderDTO;
import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Order;
import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.entity.ProductsOrder;
import com.tricol.tricol.model.enums.OrderStatus;
import com.tricol.tricol.model.mapper.OrderMapper;
import com.tricol.tricol.model.mapper.ProductsOrderMapper;
import com.tricol.tricol.repository.OrderRepository;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.service.interfaces.OrderService;
import com.tricol.tricol.service.interfaces.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final ProductsOrderMapper productsOrderMapper;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository, ProductsOrderMapper productsOrderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.productsOrderMapper = productsOrderMapper;
    }

    @Override
    public Map<String, Object> create(OrderDTO dto) {
        if (dto == null)
            throw new AppException("Les informations du commande ne peuvent pas être vides", HttpStatus.BAD_REQUEST);
        Order order = orderMapper.toEntity(dto);
        List<Product> productsUpdated = new ArrayList<>();

        List<ProductsOrder> productsOrders = dto.getProductsOrders().stream()
                .map(pod -> {
                    Map<String, Object> fifoResult = CalculAmountFIFO(pod.getProductName(), pod.getQuantity());
                    pod.setAmount(((Number) fifoResult.getOrDefault("amount", 0)).doubleValue());
                    List<Product> products = (List<Product>) fifoResult.get("products");

                    productsUpdated.addAll(products);

                    ProductsOrder productsOrder = productsOrderMapper.toEntity(pod);
                    productsOrder.setProduct(products.stream().findFirst().get());
                    return productsOrder;
                }).toList();
        double totalAmount = productsOrders.stream().mapToDouble(ProductsOrder::getAmount).sum();
        order.setTotalAmount(totalAmount);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setProductsOrders(productsOrders);

        order = orderRepository.save(order);
        if (!productsUpdated.isEmpty()) {
            productRepository.saveAll(productsUpdated);
        }

        return Map.of(
                "message", "Commande créée avec succès",
                "status", 201,
                "data", orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> findById(UUID uuid) {
        return Map.of();
    }

    @Override
    public Map<String, Object> findAll(Map<String, Object> map) {
        return Map.of();
    }

    @Override
    public Map<String, Object> update(UUID uuid, OrderDTO dto) {
        return Map.of();
    }

    @Override
    public Map<String, Object> delete(UUID uuid) {
        return Map.of();
    }

    @Override
    public Map<String, Object> findAllBySupplier(SupplierDTO dto) {
        return Map.of();
    }

    private Map<String, Object> CalculAmountFIFO(String productName, Integer quantityRequested) {
        if (productName == null)
            throw new AppException("Le produit du nom '" + productName + "' ne peuvent pas être vides", HttpStatus.BAD_REQUEST);
        List<Product> products = productRepository.findByNameOrderByUpdatedAtAsc(productName);
        if (products.isEmpty())
            throw new AppException("Produits de nom '" + productName + "' introuvable", HttpStatus.NOT_FOUND);

        int totalQty = products.stream().mapToInt(Product::getQuantity).sum();
        if (quantityRequested > totalQty)
            throw new AppException("Quantité demandée du produit '" + productName + "' supérieure au stock disponible", HttpStatus.BAD_REQUEST);

        double amount = 0;
        int quantityRest = quantityRequested;
        List<Product> listUpdated = new ArrayList<>();

        for (Product p : products) {
            if (quantityRest <= 0) break;
            if (p.getQuantity() == 0) continue;

            int useQty = Math.min(p.getQuantity(), quantityRest);
            amount += useQty * p.getUnitPrice();

            p.setQuantity(p.getQuantity() - useQty);
            listUpdated.add(p);
            quantityRest -= useQty;
        }

        return Map.of(
                "products", listUpdated,
                "amount", amount
        );
    }
}
