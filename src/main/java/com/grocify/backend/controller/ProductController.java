package com.grocify.backend.controller;

import java.util.List;

import com.grocify.backend.entity.Product;
import com.grocify.backend.service.CloudinaryService;
import com.grocify.backend.service.ProductService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CloudinaryService cloudinaryService;

    public ProductController(
            ProductService productService,
            CloudinaryService cloudinaryService
    ) {
        this.productService = productService;
        this.cloudinaryService = cloudinaryService;
    }

    // =========================
    // GET ALL PRODUCTS
    // =========================

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // =========================
    // CREATE PRODUCT
    // =========================

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    // =========================
    // UPDATE PRODUCT
    // =========================

    @PutMapping("/{id}")
    public Product updateProduct(
            @PathVariable Long id,
            @RequestBody Product product
    ) {
        return productService.updateProduct(id, product);
    }

    // =========================
    // DELETE PRODUCT
    // =========================

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    // =========================
    // TEST AUTH
    // =========================

    @GetMapping("/test-auth")
    public String testAuth() {
        return "Authenticated successfully";
    }

    // =========================
    // UPLOAD PRODUCT IMAGE
    // =========================

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            String imageUrl =
                    cloudinaryService.uploadImage(file);

            return ResponseEntity.ok(imageUrl);

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Image upload failed");
        }
    }
}