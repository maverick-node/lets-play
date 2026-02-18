package com.example.demo.Services;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.DTO.RegisterDTO;
import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.UserRepo;

import jakarta.validation.Valid;

@Service
public class RegisterServices {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;

    public RegisterServices(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> registerUser(@Valid RegisterDTO registerDTO) {

        // 1️⃣ Check required fields
        if (registerDTO.getUsername() == null || registerDTO.getPassword() == null || registerDTO.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Registration failed: Missing required fields."));
        }

        // 2️⃣ Set default role
        registerDTO.setRole("USER");

        // 3️⃣ Validate username and email uniqueness
        if (userRepo.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Registration failed: Username already taken."));
        }
        if (userRepo.findByEmailIgnoreCase(registerDTO.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Registration failed: Username already taken."));
        }

        // 4️⃣ Validate length
        if (registerDTO.getUsername().length() < 3 || registerDTO.getPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Registration failed: Username or password too short."));
        }

        // 5️⃣ Generate UUID for user
        String uuid = UUID.randomUUID().toString();

        // 6️⃣ Hash password and save user
        String hashedPassword = passwordEncoder.encode(registerDTO.getPassword());

        RegisterEntity user = new RegisterEntity();
        user.setUsername(registerDTO.getUsername().toLowerCase());
        user.setPassword(hashedPassword);
        user.setEmail(registerDTO.getEmail().toLowerCase());
        user.setRole(registerDTO.getRole());
        user.setuserUuid(uuid); // ✅ Set UUID
        userRepo.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully", "userUuid", uuid));
    }
}
