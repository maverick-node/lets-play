package com.example.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.RegisterDTO;
import com.example.demo.Services.RegisterServices;

@RestController
public class RegisterController {
    @Autowired
    private  RegisterServices registerServices;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterDTO registerDTO) {
        return registerServices.registerUser(registerDTO);

    }
}
