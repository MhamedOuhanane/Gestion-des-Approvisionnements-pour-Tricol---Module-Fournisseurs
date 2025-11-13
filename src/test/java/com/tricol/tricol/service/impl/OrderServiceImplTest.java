package com.tricol.tricol.service.impl;

import com.tricol.tricol.model.mapper.OrderMapper;
import com.tricol.tricol.repository.OrderRepository;
import com.tricol.tricol.repository.ProductRepository;
import com.tricol.tricol.repository.SupplierRepository;
import com.tricol.tricol.service.interfaces.StockMovementService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private ProductRepository productRepository;
    @Mock private SupplierRepository supplierRepository;
    @Mock private StockMovementService stockMovementService;

    @InjectMocks private OrderServiceImpl orderService;

}
