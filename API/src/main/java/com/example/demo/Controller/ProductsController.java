package com.example.demo.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.UpdatedProduct;
import com.example.demo.Services.ProductServices;

import jakarta.validation.Valid;
@RestController
public class ProductsController {
    private final ProductServices productServices;
    private final JwtUtil jwtUtil;

    public ProductsController(ProductServices productServices, JwtUtil jwtUtil) {
        this.productServices = productServices;
        this.jwtUtil = jwtUtil;
    }

    // Get all products (public)
    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        return productServices.getAll();
    }

    // Add product (requires login)
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@CookieValue(name = "jwt", required = false) String jwt,
                                        @Valid @RequestBody ProductDTO productDTO) {
        if (jwt == null || jwt.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT is missing");
        }
        String username = jwtUtil.extractUsername(jwt);
        return productServices.addProduct(username, productDTO);
    }

    // Update product (requires login + admin or owner)
    @PutMapping("/products/{id}")
    public ResponseEntity<?> putProduct(@CookieValue(name = "jwt", required = false) String jwt,
                                        @PathVariable("id") String productId,
                                        @RequestBody UpdatedProduct updatedProduct) {
        if (jwt == null || jwt.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT is missing");
        }
        String username = jwtUtil.extractUsername(jwt);
        return productServices.putProducts(username, productId, updatedProduct);
    }

    // Delete product (requires login + admin or owner)
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@CookieValue(name = "jwt", required = false) String jwt,
                                           @PathVariable("id") String productId) {
        if (jwt == null || jwt.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT is missing");
        }
        String username = jwtUtil.extractUsername(jwt);
        return productServices.deleteProduct(productId, username);
    }

    // Get all users (admin only)
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@CookieValue(name = "jwt", required = false) String jwt) {
        if (jwt == null || jwt.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT is missing");
        }
        String username = jwtUtil.extractUsername(jwt);
        return productServices.getUsers(username);
    }
}
