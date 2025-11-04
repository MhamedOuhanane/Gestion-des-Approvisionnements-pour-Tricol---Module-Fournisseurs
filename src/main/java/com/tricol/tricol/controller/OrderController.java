package com.tricol.tricol.controller;

import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.UpdateOrderStatusDTO;
import com.tricol.tricol.model.enums.OrderStatus;
import com.tricol.tricol.service.interfaces.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> shows(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String supplierContact,
            @RequestParam(required = false) String productName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Map<String, Object> map = Map.of(
                "page", page,
                "size", size,
                "status", status != null ? status : "",
                "supplierContact", supplierContact != null ? supplierContact : "",
                "productName", productName != null ? productName : ""
        );
        var result = orderService.findAll(map);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody OrderDTO dto) {
        var result = orderService.create(dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable UUID uuid) {
        var result = orderService.findById(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody OrderDTO dto
    ) {
        var result = orderService.update(uuid, dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> destroy(@PathVariable UUID uuid) {
        var result = orderService.delete(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable UUID uuid, @RequestBody UpdateOrderStatusDTO dto) {
        var result = orderService.updateStatus(uuid, dto.getOrderStatusEnum());
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }
}
