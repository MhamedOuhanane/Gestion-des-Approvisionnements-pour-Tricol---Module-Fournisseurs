package com.tricol.tricol.controller;

import com.tricol.tricol.model.dto.ProductDTO;
import com.tricol.tricol.service.interfaces.ProductService;
import com.tricol.tricol.service.interfaces.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Produits", description = "API de gestion des produits (ajout, mise à jour, suppression, recherche, mouvements de stock).")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final StockMovementService stockMovementService;

    public ProductController(ProductService productService, StockMovementService stockMovementService) {
        this.productService = productService;
        this.stockMovementService = stockMovementService;
    }

    @Operation(
            summary = "Lister les produits",
            description = "Récupère tous les produits avec pagination et filtres optionnels (nom, catégorie).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste des produits récupérée avec succès"),
                    @ApiResponse(responseCode = "400", description = "Paramètres invalides", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> shows(
            @Parameter(description = "Nom du produit") @RequestParam(required = false) String name,
            @Parameter(description = "Catégorie du produit") @RequestParam(required = false) String category,
            @Parameter(description = "Numéro de la page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "5") int size
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

    @Operation(
            summary = "Créer un produit",
            description = "Ajoute un nouveau produit ou met à jour la quantité si le produit existe déjà.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
                    @ApiResponse(responseCode = "200", description = "Produit existant mis à jour"),
                    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(
            @Parameter(description = "Données du produit à créer") @Valid @RequestBody ProductDTO dto
    ) {
        Map<String, Object> result = productService.create(dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Récupérer un produit",
            description = "Récupère les informations d’un produit par son UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produit trouvé"),
                    @ApiResponse(responseCode = "404", description = "Produit non trouvé", content = @Content)
            }
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> show(
            @Parameter(description = "UUID du produit") @PathVariable UUID uuid
    ) {
        Map<String, Object> result = productService.findById(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Mettre à jour un produit",
            description = "Met à jour les informations d’un produit existant par son UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Produit non trouvé", content = @Content)
            }
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(description = "UUID du produit") @PathVariable UUID uuid,
            @Parameter(description = "Données du produit mises à jour") @Valid @RequestBody ProductDTO dto
    ) {
        Map<String, Object> result = productService.update(uuid, dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Supprimer un produit",
            description = "Supprime un produit existant via son identifiant UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produit supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Produit non trouvé", content = @Content)
            }
    )
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> destroy(
            @Parameter(description = "UUID du produit à supprimer") @PathVariable UUID uuid
    ) {
        Map<String, Object> result = productService.delete(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Lister les mouvements de stock d’un produit",
            description = "Récupère l’historique des mouvements de stock pour un produit donné avec pagination et filtre par type (entrée/sortie)."
    )
    @GetMapping("/{productId}/stockMovements")
    public ResponseEntity<Map<String, Object>> showMovement(
            @Parameter(description = "UUID du produit") @PathVariable UUID productId,
            @Parameter(description = "Type de mouvement (entrée/sortie)") @RequestParam(required = false, defaultValue = "") String type,
            @Parameter(description = "Numéro de la page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "5") int size
    ) {
        Map<String, Object> filter = Map.of(
                "page", page,
                "size", size,
                "type", type,
                "productId", productId
        );
        var result = stockMovementService.findAll(filter);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }
}
