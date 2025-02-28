package com.example.project.service;

import com.example.project.model.Product;
import com.example.project.model.ProductDto;
import com.example.project.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setName("Product 1");
        product1.setId(1L);
        product1.setQuantity(5);
        product1.setPrice(10.0);

        product2 = new Product();
        product2.setName("Product 2");
        product2.setId(2L);
        product2.setQuantity(20);
        product2.setPrice(35.0);

    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductDto> result = productService.getAllProducts();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).name());
        assertEquals("Product 2", result.get(1).name());
    }

    @Test
    void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        Optional<ProductDto> result = productService.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().name());
        assertEquals(10.0, result.get().price());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<ProductDto> result = productService.getProductById(3L);

        assertFalse(result.isPresent());
    }
}
