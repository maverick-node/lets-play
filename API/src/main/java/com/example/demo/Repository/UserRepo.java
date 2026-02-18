package com.example.demo.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Entity.RegisterEntity;

public interface UserRepo extends MongoRepository<RegisterEntity, Integer> {
    Optional<RegisterEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Optional<RegisterEntity> findByEmailIgnoreCase(String email);

}
