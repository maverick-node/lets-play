package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Entity.ProductEntity;

public interface ProductsRepo extends MongoRepository<ProductEntity, String> {

    List<ProductEntity> findAll();
    Optional<ProductEntity> findById(String id);

}
