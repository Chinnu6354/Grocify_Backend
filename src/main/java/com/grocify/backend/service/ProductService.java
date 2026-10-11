package com.grocify.backend.service;

import com.grocify.backend.entity.Product;
import com.grocify.backend.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable("products")
    public List<Product> getAllProducts() {
        System.out.println("Fetching products from MySQL...");
        return productRepository.findAll();
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Long id, Product product) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setImage(product.getImage());
        existingProduct.setDescription(product.getDescription());

        return productRepository.save(existingProduct);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(existingProduct);
    }
}