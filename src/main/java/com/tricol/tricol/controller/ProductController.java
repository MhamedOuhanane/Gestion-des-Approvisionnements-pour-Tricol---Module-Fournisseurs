package com.tricol.tricol.controller;

import com.tricol.tricol.model.dto.ProductDTO;
import com.tricol.tricol.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Produits", description = "Gestions des produits")
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Récupérer tous les produits avec pagination et filtres facultatifs")
    @GetMapping
    public ResponseEntity<Map<String, Object>> shows(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Map<String, Object> map = Map.of(
                "page", page,
                "size", size,
                "name", name != null ? name : "",
                "category", category != null ? category : ""
        );
        Map<String, Object> result = productService.findAll(map);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(summary = "Ajouter un nouveau produit")
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ProductDTO dto) {
        Map<String, Object> result = productService.create(dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(summary = "Récupérer un produit par son UUID")
    @GetMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable UUID uuid) {
        Map<String, Object> result = productService.findById(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(summary = "Mettre à jour un produit existant par UUID")
    @PutMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody ProductDTO dto
    ) {
        Map<String, Object> result = productService.update(uuid, dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(summary = "Supprimer un produit par UUID")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> destroy(@PathVariable UUID uuid) {
        Map<String, Object> result = productService.delete(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

}
