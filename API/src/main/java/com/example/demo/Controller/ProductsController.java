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

    public ProductsController(ProductServices productService, JwtUtil jwtUtil) {
        this.productServices = productService;
        this.jwtUtil = jwtUtil;
    }

    // Get Products (Public Access)
    @GetMapping("/products")
    public ResponseEntity<?> getProduct() {
        return productServices.getAll();
    }

    // Add Products Only Logged Users
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@CookieValue(name = "jwt", required = false) String jwt,
            @Valid @RequestBody ProductDTO productDTO) {
        String username = jwtUtil.extractUsername(jwt);
        return productServices.addProduct(username, productDTO);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> putProducts(
            @CookieValue(name = "jwt", required = false) String jwt,
            @PathVariable("id") String productId, @RequestBody UpdatedProduct updatedProduct) {

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT provided");
        }
        String username = jwtUtil.extractUsername(jwt);
        productServices.putProducts(username, productId, updatedProduct);

        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@CookieValue(name = "jwt", required = false) String jwt,
            @PathVariable("id") String productId) {

        String username = jwtUtil.extractUsername(jwt);
        return productServices.deleteProduct(productId, username);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@CookieValue(name = "jwt", required = false) String jwt) {
        String username = jwtUtil.extractUsername(jwt);
        return productServices.getUsers(username);
    }

}
