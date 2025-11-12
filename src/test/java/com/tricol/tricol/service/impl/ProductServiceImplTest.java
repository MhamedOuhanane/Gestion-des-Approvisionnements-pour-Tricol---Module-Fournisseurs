package com.tricol.tricol.service.impl;

import com.tricol.tricol.model.entity.Product;
import com.tricol.tricol.model.mapper.ProductMapper;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.service.interfaces.StockMovementService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
