package com.tricol.tricol.controller;

import com.tricol.tricol.service.interfaces.OrderService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
}
