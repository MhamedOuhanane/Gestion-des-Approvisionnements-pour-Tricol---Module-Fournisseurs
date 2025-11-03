package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.ProductsOrderDTO;
import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Order;
import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.entity.ProductsOrder;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.enums.OrderStatus;
import com.tricol.tricol.model.mapper.OrderMapper;
import com.tricol.tricol.model.mapper.ProductsOrderMapper;
import com.tricol.tricol.repository.OrderRepository;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.repository.SupplierRepository;
import com.tricol.tricol.service.interfaces.OrderService;
import com.tricol.tricol.service.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final ProductsOrderMapper productsOrderMapper;
    private final SupplierRepository supplierRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository, ProductsOrderMapper productsOrderMapper, SupplierRepository supplierRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.productsOrderMapper = productsOrderMapper;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Map<String, Object> create(OrderDTO dto) {
        if (dto == null)
            throw new AppException("Les informations du commande ne peuvent pas être vides", HttpStatus.BAD_REQUEST);
        Order order = orderMapper.toEntity(dto);

        order = handleOrderProcessing(order, dto.getProductsOrders(), "PENDING");

        return Map.of(
                "message", "Commande créée avec succès",
                "status", 201,
                "data", orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> findById(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedById(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));

        return Map.of(
                "message", "Commande trouvé avec succès!",
                "status", 200,
                "data", orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> findAll(Map<String, Object> map) {
        if (map.isEmpty()) map = Map.of("page", 0, "size", 5);

        UUID supplierId = (UUID) map.getOrDefault("supplierId", null);
        String status = (String) map.getOrDefault("status", "");
        String productName = (String) map.getOrDefault("productName", "");
        String supplierContact = (String) map.getOrDefault("supplierContact", "");
        Pageable pageable = PageRequest.of((int) map.get("page"), (int) map.get("size"), Sort.by("createdAt").ascending());

        Page<Order> orders = orderRepository.findAllWithFilters(supplierContact, productName, status, supplierId, pageable);
        Map<String, Object> pagination = Map.of(
                "page", orders.getNumber(),
                "size", orders.getSize(),
                "totalElements", orders.getTotalElements(),
                "totalPages", orders.getTotalPages(),
                "isFirst", orders.isFirst(),
                "isLast", orders.isLast()
        );

        String message;
        Object data;
        if (orders.isEmpty()) {
            message = "Aucun orders n'existe dans le système";
            data = List.of();
        } else {
            message = "Les orderss trouvés avec succès";
            data = orders.stream().map(orderMapper::toDto).toList();
        }

        return Map.of(
                "message", message,
                "status", 200,
                "data", data,
                "pagination", pagination
        );
    }

    @Override
    public Map<String, Object> update(UUID uuid, OrderDTO dto) {
        if (uuid == null)
            throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        if (dto == null)
            throw new AppException("Les informations du commande ne peuvent pas être vides", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedById(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        boolean updated = false;
        if (!order.getOrderDate().equals(dto.getOrderDate())) {
            order.setOrderDate(dto.getOrderDate());
            updated = true;
        }
        if (!order.getStatus().equals(dto.getStatus())) {
            order.setStatus(dto.getStatus());
            updated = true;
        }
        if (!order.getTotalAmount().equals(dto.getTotalAmount())) {
            order.setTotalAmount(dto.getTotalAmount());
            updated = true;
        }
        if (!order.getStatus().equals(dto.getStatus())) {
            order = handleStatusTransition(order, dto.getStatus().name());
            updated = true;
        }
        if (!order.getProductsOrders().equals(dto.getProductsOrders())) {
            order = handleOrderProcessing(order, dto.getProductsOrders(), order.getStatus().name());
            updated = true;
        }
        else if (updated) order = orderRepository.save(order);

        return Map.of(
                "message", updated ? "Le commande  a été mis à jour avec succès!" : "Aucun champ du fournisseur n'a été modifié.",
                "status", 200,
                "data", orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> delete(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedById(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        orderRepository.delete(order);
        boolean deleted = !orderRepository.existsById(uuid);
        String message = deleted
                ? "La commande a été supprimé avec succès."
                : "Échec de la suppression du commande.";
        int status = deleted ? 200 : 500;
        return Map.of(
                "message", message,
                "status", status,
                "data", deleted ? Optional.empty() : orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> findAllBySupplier(UUID supplierId, Map<String, Object> map) {
        if (supplierId == null) throw new AppException("L'identifiant du supplier ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new AppException("Aucun supplier trouvé avec cet identifiant", HttpStatus.NOT_FOUND));

        map.put("supplierId", supplierId);
        return findAll(map);
    }

    @Override
    public Map<String, Object> updateStatus(UUID uuid, String status) {
        if (uuid == null)
            throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        if (status == null)
            throw new AppException("Le nouveau status ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedById(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));

        if (order.getStatus().equals(OrderStatus.valueOf(status.toUpperCase())))
            throw new AppException("Le statut est déjà " + status, HttpStatus.BAD_REQUEST);
        order = handleStatusTransition(order, status);
        orderRepository.save(order);

        if (order.getStatus() == OrderStatus.VALIDATED && status.equalsIgnoreCase("VALIDATED"))
            updateDeliveredProducts(order.getProductsOrders());


        return Map.of(
                "message", "Le commande est " + order.getStatus().getDesc() + " avec succès!",
                "status", 200,
                "data", order
        );
    }

    private Order handleOrderProcessing(Order order, List<ProductsOrderDTO> productsOrderDTOS, String status) {
        List<ProductsOrder> productsOrders = buildProductsOrders(productsOrderDTOS);
        order.setTotalAmount(calculateTotalAmount(productsOrders));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        order.setProductsOrders(productsOrders);

        order = orderRepository.save(order);
        if (OrderStatus.VALIDATED.name().equalsIgnoreCase(status)) {
            updateDeliveredProducts(productsOrders);
        }
        return  order;
    }

    private List<ProductsOrder> buildProductsOrders(List<ProductsOrderDTO> productsOrderDTOS) {
        return productsOrderDTOS.stream()
                .map(pod -> {
                    Map<String, Object> fifoResult = calculateAmountFIFO(pod.getProductName(), pod.getQuantity());
                    pod.setAmount(((Number) fifoResult.getOrDefault("amount", 0)).doubleValue());
                    List<Product> products = (List<Product>) fifoResult.get("products");

                    ProductsOrder productsOrder = productsOrderMapper.toEntity(pod);
                    productsOrder.setProduct(products.stream().findFirst().orElseThrow());
                    return productsOrder;
                }).toList();
    }

    private double calculateTotalAmount(List<ProductsOrder> productsOrders) {
        return productsOrders.stream()
                .mapToDouble(ProductsOrder::getAmount)
                .sum();
    }

    private void updateDeliveredProducts(List<ProductsOrder> productsOrders) {
        List<Product> products = productsOrders.stream()
                .map(ProductsOrder::getProduct)
                .filter(Objects::nonNull)
                .toList();
        if (!products.isEmpty()) {
            productRepository.saveAll(products);
        }
    }

    private Map<String, Object> calculateAmountFIFO(String productName, Integer quantityRequested) {
        if (productName == null)
            throw new AppException("Le nom du produit ne peuvent pas être vides", HttpStatus.BAD_REQUEST);
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

    private Order handleStatusTransition(Order order, String status) {
        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());

        if (order.getStatus().equals(newStatus))
            return order;

        switch (newStatus) {
            case CANCELED, VALIDATED -> {
                if (order.getStatus() != OrderStatus.PENDING)
                    throw new AppException("Transition non autorisée : seule une commande 'PENDING' peut être " + newStatus.getDesc(), HttpStatus.BAD_REQUEST);
                order.setStatus(newStatus);
            }
            case DELIVERED -> {
                if (order.getStatus() != OrderStatus.VALIDATED)
                    throw new AppException("Transition non autorisée : seule une commande 'VALIDATED' peut être 'DELIVERED'", HttpStatus.BAD_REQUEST);
                order.setStatus(newStatus);
            }
            case PENDING -> throw new AppException("Transition non autorisée : Le statut ne peut pas être modifié en « en attente ».", HttpStatus.BAD_REQUEST);
            default -> throw new AppException("Transition de statut non reconnue", HttpStatus.BAD_REQUEST);
        }

        return order;
    }

}
