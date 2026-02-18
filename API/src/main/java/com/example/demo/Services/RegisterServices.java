package com.example.demo.Services;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.DTO.RegisterDTO;
import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.UserRepo;

import jakarta.annotation.PostConstruct;

@Service
public class RegisterServices {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public RegisterServices(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> registerUser(@Validated RegisterDTO registerDTO) {
        // Registration logic goes here (e.g., save to database)
        if (registerDTO.getUsername() == null || registerDTO.getPassword() == null || registerDTO.getEmail() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Registration failed: Missing required fields."));
        }
        registerDTO.setRole("USER");

        if (userRepo.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.badRequest().body("This username has been taken");
        }
        if (userRepo.existsByEmail(registerDTO.getUsername())) {
            return ResponseEntity.badRequest().body("This email has been taken");
        }
        if (registerDTO.getUsername().length() < 3 || registerDTO.getPassword().length() < 6) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Registration failed: Username or password too short."));
        }
        String uuid = UUID.randomUUID().toString();

        // Hashing password and saving user logic would go here
        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());
        RegisterEntity Save = new RegisterEntity();
        Save.setUsername(registerDTO.getUsername());
        Save.setPassword(hashedPassword);
        Save.setEmail(registerDTO.getEmail());
        Save.setRole(registerDTO.getRole());
        Save.setuserUuid(uuid);
        userRepo.save(Save);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
}
