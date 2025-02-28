package com.example.project.model;

public record ProductDto(Long id,
                         String name,
                         int quantity,
                         double price) {

    public static ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getQuantity(), product.getPrice());
    }

    public static Product toEntity(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.id());
        product.setName(productDto.name());
        product.setQuantity(productDto.quantity());
        product.setPrice(productDto.price());
        return product;
    }
}
