package com.example.demo.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.UpdatedProduct;
import com.example.demo.DTO.UsersDTO;
import com.example.demo.Entity.ProductEntity;
import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.ProductsRepo;
import com.example.demo.Repository.UserRepo;

@Service
public class ProductServices {
    private final ProductsRepo productRepo;
    private final UserRepo userRepo;

    public ProductServices(ProductsRepo productRepo, UserRepo userRepo) {
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    // Get all products
    public ResponseEntity<?> getAll() {
        var products = productRepo.findAll();
        return ResponseEntity.ok(products);
    }

    // Add new product
    public ResponseEntity<?> addProduct(String username, ProductDTO productDTO) {
        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not logged in"); // 401 Unauthorized
        }
        RegisterEntity user = userOpt.get();

        ProductEntity product = new ProductEntity();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setUserId(user.getId());
        product.setCreatedAt(LocalDateTime.now());

        productRepo.save(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Product added successfully"); // 201 Created
    }

    // Update existing product
    public ResponseEntity<?> putProducts(String username, String productId, UpdatedProduct updatedProduct) {

        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found or not logged in"); // 401 Unauthorized
        }
        RegisterEntity user = userOpt.get();

        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found"); // 404 Not Found
        }
        ProductEntity product = productOpt.get();

        // Admin or owner check
        if (!user.getRole().equalsIgnoreCase("admin") && !user.getId().equals(product.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to update this product"); // 403 Forbidden
        }

        boolean updated = false;
        if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank()) {
            product.setName(updatedProduct.getName());
            updated = true;
        }
        if (updatedProduct.getDescription() != null && !updatedProduct.getDescription().isBlank()) {
            product.setDescription(updatedProduct.getDescription());
            updated = true;
        }
        if (updatedProduct.getPrice() != null && !updatedProduct.getPrice().equals(product.getPrice())) {
            product.setPrice(updatedProduct.getPrice());
            updated = true;
        }

        if (!updated) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No changes detected in the request"); // 409 Conflict
        }

        productRepo.save(product);

        return ResponseEntity.ok("Product has been updated successfully"); // 200 OK
    }

    // Delete product
    public ResponseEntity<?> deleteProduct(String productId, String username) {
        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found or not logged in"); // 401 Unauthorized
        }
        RegisterEntity user = userOpt.get();

        Optional<ProductEntity> productOpt = productRepo.findById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found"); // 404 Not Found
        }
        ProductEntity product = productOpt.get();

        if (!user.getRole().equalsIgnoreCase("admin") && !user.getId().equals(product.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to delete this product"); // 403 Forbidden
        }

        productRepo.deleteById(productId);
        return ResponseEntity.ok("Product has been deleted"); // 200 OK
    }

    // Get all users (admin only)
    public ResponseEntity<?> getUsers(String username) {
        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not found or not logged in"); // 401 Unauthorized
        }
        RegisterEntity user = userOpt.get();

        if (!user.getRole().equalsIgnoreCase("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not admin"); // 403 Forbidden
        }

        List<UsersDTO> usersDTO = userRepo.findAll()
                .stream()
                .map(u -> new UsersDTO(u.getUsername(), u.getEmail(), u.getRole(), u.getuserUuid()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(usersDTO);
    }
}
