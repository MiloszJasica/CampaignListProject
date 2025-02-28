package com.example.project.service;

import com.example.project.model.Product;
import com.example.project.model.ProductDto;
import com.example.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ProductDto> getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductDto::toDto);
    }

    public Long createProduct(ProductDto product) {
        Product save = productRepository.save(ProductDto.toEntity(product));
        return save.getId();
    }
}
