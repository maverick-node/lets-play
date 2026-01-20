package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Entity.RegisterEntity;

public interface UserRepo extends MongoRepository<RegisterEntity, String> {
    Optional<RegisterEntity> findByUsername(String username);
}
