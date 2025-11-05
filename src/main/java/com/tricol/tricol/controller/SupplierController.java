package com.tricol.tricol.controller;

import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.service.interfaces.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Tag(name = "Fournisseurs", description = "API de gestion des fournisseurs (création, mise à jour, suppression, recherche).")
@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Operation(
            summary = "Lister les fournisseurs",
            description = "Récupère la liste paginée des fournisseurs avec des filtres optionnels (ville, société, contact).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
                    @ApiResponse(responseCode = "400", description = "Paramètres invalides", content = @Content)
            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> shows(
            @Parameter(description = "Ville du fournisseur") @RequestParam(required = false) String city,
            @Parameter(description = "Nom de la société") @RequestParam(required = false) String company,
            @Parameter(description = "Contact du fournisseur") @RequestParam(required = false) String contact,
            @Parameter(description = "Numéro de la page") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "5") int size
    ) {
        Map<String, Object> parms = Map.of(
                "city", city != null ? city : "",
                "company", company != null ? company : "",
                "contact", contact != null ? contact : "",
                "page", page,
                "size", size
        );
        Map<String, Object> result = supplierService.findAll(parms);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Créer un fournisseur",
            description = "Ajoute un nouveau fournisseur dans le système.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Fournisseur créé avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
            }
    )
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody SupplierDTO dto) {
        Map<String, Object> result = supplierService.create(dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Récupérer un fournisseur",
            description = "Récupère un fournisseur par son identifiant UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Fournisseur trouvé"),
                    @ApiResponse(responseCode = "404", description = "Fournisseur non trouvé", content = @Content)
            }
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> show(@PathVariable UUID uuid) {
        Map<String, Object> result = supplierService.findById(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Mettre à jour un fournisseur",
            description = "Met à jour les informations d’un fournisseur existant via son UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Fournisseur mis à jour avec succès"),
                    @ApiResponse(responseCode = "400", description = "Données invalides", content = @Content)
            }
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody SupplierDTO dto
    ) {
        Map<String, Object> result = supplierService.update(uuid, dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Supprimer un fournisseur",
            description = "Supprime un fournisseur existant par son identifiant UUID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Fournisseur supprimé avec succès"),
                    @ApiResponse(responseCode = "404", description = "Fournisseur non trouvé", content = @Content)
            }
    )
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID uuid) {
        Map<String, Object> result = supplierService.delete(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Rechercher un fournisseur par e-mail",
            description = "Récupère un fournisseur en fonction de son adresse e-mail."
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> findByEmail(@PathVariable String email) {
        Map<String, Object> result = supplierService.findByEmail(email);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @Operation(
            summary = "Rechercher un fournisseur par code ICE",
            description = "Récupère un fournisseur à partir de son code ICE unique."
    )
    @GetMapping("/ice/{ice}")
    public ResponseEntity<Map<String, Object>> findByIce(@PathVariable String ice) {
        Map<String, Object> result = supplierService.findByIce(ice);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }
}
