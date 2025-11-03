package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.ProductDTO;
import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.mapper.ProductMapper;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.service.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }


    @Override
    public Map<String, Object> create(ProductDTO dto) {
        if (dto == null)
            throw new AppException("Les informations du produit ne peuvent pas être vides", HttpStatus.BAD_REQUEST);

        String message;
        int status = 201;

        Optional<Product> ProductOpt = productRepository.findByNameAndUnitPrice(dto.getName(), dto.getUnitPrice());

        Product product;
        if (ProductOpt.isPresent()) {
            product = ProductOpt.get();
            product.setQuantity(product.getQuantity() + dto.getQuantity());
            message = "Les informations du produit existant mise à jour";
            status = 200;
        } else if (!productRepository.findByName(dto.getName()).isEmpty()) {
                product = productMapper.toEntity(dto);
                message = "Nouveau produit créé avec le même nom '" + product.getName() + "' et prix différent";
        } else {
            product = productMapper.toEntity(dto);
            message = "Le produit '" + product.getName() + "' a été ajouté avec succès!";
        }
        product = productRepository.save(product);
        if (dto.getUuid() != null && !dto.getUuid().equals(product.getUuid())) {
            productRepository.delete(productMapper.toEntity(dto));
        }

        return Map.of(
                "message", message,
                "status", status,
                "data", productMapper.toDto(product)
        );
    }


    @Override
    public Map<String, Object> findById(UUID uuid) {
        if (uuid == null) throw new AppException("L'identifiant du produit ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new AppException("Aucun produit trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        return Map.of(
                "message", "Le produit trouvé avec succès!",
                "status", 200,
                "data", productMapper.toDto(product)
        );
    }

    @Override
    public Map<String, Object> findAll(Map<String, Object> map) {
        if (map.isEmpty()) map = Map.of("page", 0, "size", 5);
        Pageable pageable = PageRequest.of((int) map.get("page"), (int) map.get("size"), Sort.by("name").ascending());
        String name = (String) map.getOrDefault("name", "");
        String category = (String) map.getOrDefault("category", "");
        Page<Product> products = productRepository.findByNameContainingAndCategoryContaining(pageable, name, category);
        String message;
        Object data;
        if (products.isEmpty()) {
            message = "Aucun produit n'existe dans le système";
            data = List.of();
        } else {
            message = "Tous les produits trouvés avec succès";
            data = products.stream()
                    .map(productMapper::toDto)
                    .toList();
        }
        Object pagination = Map.of(
                "page", products.getNumber(),
                "size", products.getSize(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages(),
                "isFirst", products.isFirst(),
                "isLast", products.isLast()
        );

        return Map.of(
                "message", message,
                "status", 200,
                "data", data,
                "pagination", pagination
        );
    }

    @Override
    public Map<String, Object> update(UUID uuid, ProductDTO dto) {
        if (uuid == null)
            throw new AppException("L'identifiant du produit ne peut pas être vide", HttpStatus.BAD_REQUEST);
        if (dto == null)
            throw new AppException("Les informations du produit ne peuvent pas être vides", HttpStatus.BAD_REQUEST);
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new AppException("Aucun produit trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        boolean updated = false;

        if (!product.getName().equals(dto.getName())) {
            product.setName(dto.getName());
            updated = true;
        }
        if (!product.getCategory().equals(dto.getCategory())) {
            product.setCategory(dto.getCategory());
            updated = true;
        }
        if (!product.getDescription().equals(dto.getDescription())) {
            product.setDescription(dto.getDescription());
            updated = true;
        }
        if (!product.getUnitPrice().equals(dto.getUnitPrice())) {
            product.setUnitPrice(dto.getUnitPrice());
            updated = true;
        }
        if (!product.getQuantity().equals(dto.getQuantity())) {
            product.setQuantity(dto.getQuantity());
            updated = true;
        }

        if (updated) {
            return create(productMapper.toDto(product));
        } else {
            return Map.of(
                    "message", "Aucun champ du fournisseur n'a été modifié.",
                    "status", 200,
                    "data", productMapper.toDto(product)
            );
        }
    }

    @Override
    public Map<String, Object> delete(UUID uuid) {
        if (uuid == null)
            throw new AppException("L'identifiant du produit ne peut pas être vide", HttpStatus.BAD_REQUEST);
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new AppException("Aucun produit trouvé avec cet identifiant", HttpStatus.NOT_FOUND));
        productRepository.delete(product);
        boolean deleted = productRepository.findById(uuid).isPresent();
        String message = !deleted
                ? "Le fournisseur a été supprimé avec succès."
                : "Échec de la suppression du fournisseur.";
        int status = deleted ? 200 : 500;
        return Map.of(
                "message", message,
                "status", status,
                "data", !deleted ? Optional.empty() : productMapper.toDto(product)
        );
    }
}
