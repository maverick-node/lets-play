package com.example.demo.Services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ProductDTO;
import com.example.demo.DTO.UpdatedProduct;
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

    public ResponseEntity<?> getAll() {
        var List = productRepo.findAll();
        return ResponseEntity.ok(List);
    }

    public ResponseEntity<?> addProduct(String username, ProductDTO productDTO) {
        Optional<RegisterEntity> username1 = userRepo.findByUsername(username);
        if (username1.isEmpty() || username1 == null) {
            return ResponseEntity.badRequest().body("You are not logged");
        }
        ProductEntity pro = new ProductEntity();
        pro.setDescription(productDTO.getDescription());
        pro.setName(productDTO.getName());
        pro.setPrice(productDTO.getPrice());
        pro.setUserId(username1.get().getId());
        pro.setCreatedAt(LocalDateTime.now());
        productRepo.save(pro);
        return ResponseEntity.ok("Product Added");
    }

    public ResponseEntity<?> putProducts(String username, String productId, UpdatedProduct updatedProduct) {

        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        Optional<ProductEntity> productOpt = productRepo.findById(productId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        if (productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Product not found");
        }

        RegisterEntity user = userOpt.get();
        ProductEntity product = productOpt.get();

        if (!user.getRole().equalsIgnoreCase("admin") && !user.getId().equals(product.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin or not the owner");
        }

        if (updatedProduct.getName() != null && !updatedProduct.getName().isBlank()) {
            product.setName(updatedProduct.getName());
        }
        if (updatedProduct.getDescription() != null && !updatedProduct.getDescription().isBlank()) {
            product.setDescription(updatedProduct.getDescription());
        }
        if (updatedProduct.getPrice() != null && !updatedProduct.getPrice().equals(product.getPrice())) {
            product.setPrice(updatedProduct.getPrice());
        }

        productRepo.save(product);

        return ResponseEntity.ok("Product has been updated successfully");
    }

    public ResponseEntity<?> deleteProduct(String productid, String username) {
        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        Optional<ProductEntity> productOpt = productRepo.findById(productid);
        RegisterEntity user = userOpt.get();
        ProductEntity product = productOpt.get();

        if (!user.getRole().equalsIgnoreCase("admin") && !user.getId().equals(product.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin or not the owner");
        }
        productRepo.deleteById(productid);
        return ResponseEntity.ok("Product has been deleted");
    }

    public ResponseEntity<?> getUsers(String username) {
        Optional<RegisterEntity> userOpt = userRepo.findByUsername(username);
        RegisterEntity user = userOpt.get();
        if (!user.getRole().equalsIgnoreCase("admin")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not admin");
        }
        var users = userRepo.findAll();
        return ResponseEntity.ok(users);

    }
}
