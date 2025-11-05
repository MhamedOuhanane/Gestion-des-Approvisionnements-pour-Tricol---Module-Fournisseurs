package com.tricol.tricol.controller;

import com.tricol.tricol.model.dto.OrderDTO;
import com.tricol.tricol.model.dto.UpdateOrderStatusDTO;
import com.tricol.tricol.service.interfaces.OrderService;
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

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Commandes", description = "Gestion des commandes")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(
            summary = "Liste des commandes",
            description = "Récupère la liste paginée des commandes avec filtres optionnels.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
                    @ApiResponse(responseCode = "400", description = "Paramètres invalides", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> shows(
            @Parameter(description = "Statut de la commande") @RequestParam(required = false) String status,
            @Parameter(description = "Contact du fournisseur") @RequestParam(required = false) String supplierContact,
            @Parameter(description = "Nom du produit") @RequestParam(required = false) String productName,
            @Parameter(description = "Page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "5") int size
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
    @Operation(
            summary = "Créer une commande",
            description = "Crée une nouvelle commande avec ses produits.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Commande créée avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> create(
            @Parameter(description = "Données de la commande") @Valid @RequestBody OrderDTO dto
    ) {
        var result = orderService.create(dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @GetMapping("/{uuid}")
    @Operation(
            summary = "Récupérer une commande",
            description = "Récupère une commande par son identifiant UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Commande trouvée"),
                    @ApiResponse(responseCode = "404", description = "Commande non trouvée", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> show(
            @Parameter(description = "UUID de la commande") @PathVariable UUID uuid
    ) {
        var result = orderService.findById(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @PutMapping("/{uuid}")
    @Operation(
            summary = "Mettre à jour une commande",
            description = "Met à jour les informations d'une commande existante.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Commande mise à jour"),
                    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Commande non trouvée", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(description = "UUID de la commande") @PathVariable UUID uuid,
            @Parameter(description = "Données mises à jour") @Valid @RequestBody OrderDTO dto
    ) {
        var result = orderService.update(uuid, dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @DeleteMapping("/{uuid}")
    @Operation(
            summary = "Supprimer une commande",
            description = "Supprime une commande par son identifiant UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Commande supprimée"),
                    @ApiResponse(responseCode = "404", description = "Commande non trouvée", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> destroy(
            @Parameter(description = "UUID de la commande") @PathVariable UUID uuid
    ) {
        var result = orderService.delete(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @PatchMapping("/{uuid}")
    @Operation(
            summary = "Mettre à jour le statut d'une commande",
            description = "Met à jour le statut d'une commande (PENDING, VALIDATED, DELIVERED, CANCELED).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statut mis à jour avec succès"),
                    @ApiResponse(responseCode = "400", description = "Transition de statut non autorisée", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Commande non trouvée", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> updateStatus(
            @Parameter(description = "UUID de la commande") @PathVariable UUID uuid,
            @Parameter(description = "Nouveau statut de la commande") @RequestBody @Valid UpdateOrderStatusDTO dto
    ) {
        var result = orderService.updateStatus(uuid, dto.getOrderStatusEnum());
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @GetMapping("/supplier/{supplierId}")
    @Operation(
            summary = "Liste des commandes d'un fournisseur",
            description = "Récupère toutes les commandes d'un fournisseur donné avec pagination et filtres optionnels.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
                    @ApiResponse(responseCode = "404", description = "Fournisseur non trouvé", content = @Content)
            }
    )
    public ResponseEntity<Map<String, Object>> ordersSupplier(
            @Parameter(description = "Statut de la commande") @RequestParam(required = false) String status,
            @Parameter(description = "Contact du fournisseur") @RequestParam(required = false) String supplierContact,
            @Parameter(description = "Nom du produit") @RequestParam(required = false) String productName,
            @Parameter(description = "UUID du fournisseur") @PathVariable UUID supplierId,
            @Parameter(description = "Page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "5") int size
    ) {
        Map<String, Object> map = Map.of(
                "page", page,
                "size", size,
                "status", status != null ? status : "",
                "supplierId", supplierId,
                "supplierContact", supplierContact != null ? supplierContact : "",
                "productName", productName != null ? productName : ""
        );
        var result = orderService.findAll(map);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }
}
