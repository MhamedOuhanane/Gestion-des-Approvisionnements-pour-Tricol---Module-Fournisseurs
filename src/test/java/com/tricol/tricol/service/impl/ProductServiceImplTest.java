package com.tricol.tricol.service.impl;

import com.tricol.tricol.exception.AppException;
import com.tricol.tricol.model.dto.ProductDTO;
import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.enums.StockMovementType;
import com.tricol.tricol.model.mapper.ProductMapper;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.service.interfaces.StockMovementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private StockMovementService stockMovementService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void createProduct_shouldSucceed_whenNewProduct() {
        UUID uuid = UUID.randomUUID();

        ProductDTO dto = new ProductDTO();
        dto.setName("Fermetures éclair");
        dto.setCategory("Accessoires");
        dto.setDescription("Fermetures éclair de haute qualité pour vêtements professionnels");
        dto.setUnitPrice(2.);
        dto.setQuantity(400);

        Product product = new Product();
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setUnitPrice(dto.getUnitPrice());
        product.setQuantity(dto.getQuantity());

        when(productRepository.findByNameAndUnitPrice(dto.getName(), dto.getUnitPrice())).thenReturn(Optional.empty());
        when(productRepository.findByNameOrderByUpdatedAtAsc(dto.getName())).thenReturn(List.of());
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(dto);

        Map<String, Object> result = productService.create(dto);

        assertEquals(201, result.get("status"));
        assertEquals("Le produit '" + product.getName() + "' a été ajouté avec succès!", result.get("message"));
        assertEquals(dto, result.get("data"));

        verify(stockMovementService).create(eq(product), eq(dto.getQuantity()), eq(StockMovementType.ENTREE));
    }

    @Test
    public void createProduct_shouldUpdateQuantity_whenProductAlreadyExists() {
        UUID uuid = UUID.randomUUID();

        ProductDTO dto = new ProductDTO();
        dto.setName("Fermetures éclair");
        dto.setCategory("Accessoires");
        dto.setDescription("Fermetures éclair de haute qualité pour vêtements professionnels");
        dto.setUnitPrice(2.);
        dto.setQuantity(400);

        Product product = new Product();
        product.setUuid(uuid);
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setUnitPrice(dto.getUnitPrice());
        product.setQuantity(dto.getQuantity());

        when(productRepository.findByNameAndUnitPrice(dto.getName(), dto.getUnitPrice())).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(dto);

        Map<String, Object> result = productService.create(dto);

        assertEquals(200, result.get("status"));
        assertEquals("La quantité du produit existant a été augmentée avec succès.", result.get("message"));
        assertEquals(dto, result.get("data"));

        verify(stockMovementService).create(eq(product), eq(dto.getQuantity()), eq(StockMovementType.ENTREE));
    }

    @Test
    public void  createProduct_shouldCreateNewProduct_whenSameNameDifferentPrice() {
        UUID uuid = UUID.randomUUID();

        ProductDTO dto = new ProductDTO();
        dto.setName("Fermetures éclair");
        dto.setCategory("Accessoires");
        dto.setDescription("Fermetures éclair de haute qualité pour vêtements professionnels");
        dto.setUnitPrice(2.);
        dto.setQuantity(400);

        Product product = new Product();
        product.setUuid(uuid);
        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setDescription(dto.getDescription());
        product.setUnitPrice(dto.getUnitPrice());
        product.setQuantity(dto.getQuantity());

        when(productRepository.findByNameAndUnitPrice(dto.getName(), dto.getUnitPrice())).thenReturn(Optional.empty());
        when(productRepository.findByNameOrderByUpdatedAtAsc(dto.getName())).thenReturn(List.of(product));
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(dto);

        Map<String, Object> result = productService.create(dto);

        assertEquals(201, result.get("status"));
        assertEquals("Nouveau produit créé avec le même nom '" + product.getName() + "' et prix différent", result.get("message"));
        assertEquals(dto, result.get("data"));

        verify(stockMovementService).create(eq(product), eq(dto.getQuantity()), eq(StockMovementType.ENTREE));
    }

    @Test
    public void createProduct_shouldThrowException_whenDtoIsNull() {
        AppException exception = assertThrows(AppException.class, () -> productService.create(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Les informations du produit ne peuvent pas être vides", exception.getMessage());
    }

    @Test
    public void findProductById_shouldSucceed_whenProductIsFound() {
        UUID uuid = UUID.randomUUID();
        Product product = new Product();
        product.setUuid(uuid);
        product.setName("Fermetures éclair");
        product.setCategory("Accessoires");
        product.setDescription("Fermetures éclair de haute qualité pour vêtements professionnels");
        product.setUnitPrice(2.);
        product.setQuantity(400);

        ProductDTO dto = new ProductDTO();
        dto.setUuid(product.getUuid());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setQuantity(product.getQuantity());

        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(dto);

        Map<String, Object> result = productService.findById(uuid);

        ProductDTO data = (ProductDTO) result.get("data");

        assertEquals(200, result.get("status"));
        assertEquals("Le produit trouvé avec succès!", result.get("message"));
        assertEquals(dto, data);
        assertEquals(dto.getUuid(), data.getUuid());
        assertEquals(dto.getName(), data.getName());
    }

    @Test
    public void findProductById_shouldThrowException_whenUuidIsNull() {
        AppException exception = assertThrows(AppException.class, () -> productService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("L'identifiant du produit ne peut pas être vide", exception.getMessage());
    }

    @Test
    public void findProductById_shouldThrowException_whenProductNotFound() {
        UUID uuid = UUID.randomUUID();

        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> productService.findById(uuid));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Aucun produit trouvé avec cet identifiant", exception.getMessage());
    }



}
