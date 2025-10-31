package com.tricol.tricol.controller;

import com.tricol.tricol.model.dto.SupplierDTO;
import com.tricol.tricol.service.interfaces.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String contact,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
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

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody SupplierDTO dto) {
        Map<String, Object> result = supplierService.create(dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> findById(@PathVariable UUID uuid) {
        Map<String, Object> result = supplierService.findById(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable UUID uuid,
            @Valid @RequestBody SupplierDTO dto
    ) {
        Map<String, Object> result = supplierService.update(uuid, dto);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable UUID uuid) {
        Map<String, Object> result = supplierService.delete(uuid);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> findByEmail(@PathVariable String email) {
        Map<String, Object> result = supplierService.findByEmail(email);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

    @GetMapping("/ice/{ice}")
    public ResponseEntity<Map<String, Object>> findByIce(@PathVariable String ice) {
        Map<String, Object> result = supplierService.findByIce(ice);
        return ResponseEntity.status((int) result.getOrDefault("status", 200)).body(result);
    }

}