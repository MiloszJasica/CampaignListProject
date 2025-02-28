package com.example.project.controller;

import com.example.project.model.ProductDto;
import com.example.project.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductDto> products = Arrays.asList(
                new ProductDto(1L, "Laptop", 3000, 5),
                new ProductDto(2L, "Smartphone", 1500, 10)
        );

        when(productService.getAllProducts()).thenReturn(products);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Smartphone"));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        ProductDto product = new ProductDto(1L, "Laptop", 3000, 5);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(5));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDto newProduct = new ProductDto(null, "Tablet", 2000, 3);
        when(productService.createProduct(any(ProductDto.class))).thenReturn(3L);

        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Tablet\",\"price\":2000.0,\"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}
