package com.example.demo.Services;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.demo.Config.JwtUtil;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.Entity.RegisterEntity;
import com.example.demo.Repository.UserRepo;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class LoginServices {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginServices(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<Map<String, String>> loginUser(@Validated LoginDTO loginDTO, HttpServletResponse response) {

        Optional<RegisterEntity> userOpt = userRepo.findByUsername(loginDTO.getUsername());

        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid credentials.")); // 401 Unauthorized
        }

        RegisterEntity user = userOpt.get();

        // 1️⃣ Generate JWT with UUID in claim
        String token = jwtUtil.generateToken(user.getUsername(), user.getuserUuid());

        // 2️⃣ Set JWT as HttpOnly cookie
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 60) // 1 hour
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // 3️⃣ Return success message and optionally token in body
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "jwt", token
        ));
    }
}
