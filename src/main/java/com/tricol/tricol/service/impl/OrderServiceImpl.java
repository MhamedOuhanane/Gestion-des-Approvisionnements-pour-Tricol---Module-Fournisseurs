package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.ProductsOrderDTO;
import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.model.entity.Order;
import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.entity.ProductsOrder;
import com.tricol.tricol.model.entity.Supplier;
import com.tricol.tricol.model.entity.id.ProductsOrderId;
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
import java.util.stream.Collectors;

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
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new AppException("Aucun fournisseur trouvé avec cet identifiant", HttpStatus.NOT_FOUND));

        Order order = orderMapper.toEntity(dto);
        order.setSupplier(supplier);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(0.);
        order = orderRepository.save(order);

        List<ProductsOrder> productsOrders = buildProductsOrders(order, dto.getProductsOrders());
        order.setTotalAmount(calculateTotalAmount(productsOrders));
        order.getProductsOrders().addAll(productsOrders);

        order = orderRepository.save(order);

        return Map.of(
                "message", "Commande créée avec succès",
                "status", 201,
                "data", orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> findById(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedByUuid(uuid)
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
        OrderStatus status = !Objects.equals((String) map.get("status"), "")
                ? OrderStatus.valueOf((String) map.get("status"))
                : null;
        String productName = (String) map.getOrDefault("productName", "");
        String supplierContact = (String) map.getOrDefault("supplierContact", "");
        Pageable pageable = PageRequest.of((int) map.get("page"), (int) map.get("size"), Sort.by("orderDate").ascending());

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
            data = orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
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
        Order order = orderRepository.findDetailedByUuid(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        boolean hasChanges = false;
        List<ProductsOrder> existing = order.getProductsOrders();
        List<ProductsOrder> updated = buildProductsOrders(order, dto.getProductsOrders());

        OrderStatus oldStatus = order.getStatus();

        if (order.getOrderDate() != null && !order.getOrderDate().equals(dto.getOrderDate())) {
            order.setOrderDate(dto.getOrderDate());
            hasChanges = true;
        }
        if (order.getStatus() != null && !order.getStatus().equals(dto.getStatus())) {
            order = handleStatusTransition(order, dto.getStatus());
            hasChanges = true;
        }
        if (existing.size() != updated.size() || !new HashSet<>(existing).containsAll(updated)) {
            order.getProductsOrders().clear();
            order.getProductsOrders().addAll(updated);
            order.setTotalAmount(calculateTotalAmount(updated));
            hasChanges = true;
        }
        if (hasChanges) {
            order = orderRepository.save(order);
            if (oldStatus == OrderStatus.PENDING && order.getStatus() == OrderStatus.VALIDATED)
                updateDeliveredProducts(order.getProductsOrders());
        }

        return Map.of(
                "message", hasChanges ? "Le commande  a été mis à jour avec succès!" : "Aucun champ du fournisseur n'a été modifié.",
                "status", 200,
                "data", orderMapper.toDto(order)
        );
    }

    @Override
    public Map<String, Object> delete(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedByUuid(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CANCELED)
            throw new AppException("Transition non autorisée : seule une commande 'Annulé' ou 'En attente' peut être supprimer", HttpStatus.BAD_REQUEST);
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
    public Map<String, Object> updateStatus(UUID uuid, OrderStatus status) {
        if (uuid == null)
            throw new AppException("L'identifiant du commande ne peut pas être vide", HttpStatus.BAD_REQUEST);
        if (status == null)
            throw new AppException("Le nouveau status ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Order order = orderRepository.findDetailedByUuid(uuid)
                .orElseThrow(() -> new AppException("Aucun commande trouvé avec cet identifiant", HttpStatus.NOT_FOUND));

        if (order.getStatus() == status)
            throw new AppException("Le statut est déjà " + status.getDesc(), HttpStatus.BAD_REQUEST);
        order = handleStatusTransition(order, status);
        Order newOrder = orderRepository.save(order);

        if (status == OrderStatus.VALIDATED && newOrder.getStatus() == status)
            updateDeliveredProducts(newOrder.getProductsOrders());


        return Map.of(
                "message", "Le commande est " + newOrder.getStatus().getDesc() + " avec succès!",
                "status", 200,
                "data", newOrder
        );
    }

    private List<ProductsOrder> buildProductsOrders(Order order,List<ProductsOrderDTO> productsOrderDTOS) {
        return productsOrderDTOS.stream()
                .map(pod -> {
                    Map<String, Object> fifoResult = calculateAmountFIFO(pod.getProductName(), pod.getQuantity());
                    pod.setAmount(((Number) fifoResult.getOrDefault("amount", 0)).doubleValue());

                    List<Product> products = (List<Product>) fifoResult.get("products");
                    Product product = products.stream()
                            .findFirst()
                            .orElseThrow(() -> new AppException("Produit introuvable après FIFO", HttpStatus.NOT_FOUND));

                    return getProductsOrder(order, pod, product);
                }).collect(Collectors.toList());
    }

    private static ProductsOrder getProductsOrder(Order order, ProductsOrderDTO pod, Product product) {
        ProductsOrder productsOrder = new ProductsOrder();
        productsOrder.setQuantity(pod.getQuantity());
        productsOrder.setAmount(pod.getAmount());
        productsOrder.setProduct(product);
        productsOrder.setOrder(order);

        ProductsOrderId id = new ProductsOrderId();
        id.setOrderId(order.getUuid());
        id.setProductId(product.getUuid());
        productsOrder.setId(id);
        return productsOrder;
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
                .collect(Collectors.toList());
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

    private Order handleStatusTransition(Order order, OrderStatus status) {
        if (order.getStatus().equals(status))
            return order;

        switch (status) {
            case CANCELED, VALIDATED -> {
                if (order.getStatus() != OrderStatus.PENDING)
                    throw new AppException("Transition non autorisée : seule une commande 'PENDING' peut être " + status.getDesc(), HttpStatus.BAD_REQUEST);
                order.setStatus(status);
            }
            case DELIVERED -> {
                if (order.getStatus() != OrderStatus.VALIDATED)
                    throw new AppException("Transition non autorisée : seule une commande 'VALIDATED' peut être 'DELIVERED'", HttpStatus.BAD_REQUEST);
                order.setStatus(status);
            }
            case PENDING -> throw new AppException("Transition non autorisée : Le statut ne peut pas être modifié en « en attente ».", HttpStatus.BAD_REQUEST);
            default -> throw new AppException("Transition de statut non reconnue", HttpStatus.BAD_REQUEST);
        }

        return order;
    }

}
