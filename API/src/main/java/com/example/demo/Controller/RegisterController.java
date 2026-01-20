package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.RegisterDTO;
import com.example.demo.Services.RegisterServices;

@RestController
public class RegisterController {
    private final RegisterServices registerServices;

    public RegisterController(RegisterServices registerServices) {
        this.registerServices = registerServices;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Validated @RequestBody RegisterDTO registerDTO) {
        return registerServices.registerUser(registerDTO);

    }
}
