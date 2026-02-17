package com.example.demo.Controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.Services.LoginServices;


import jakarta.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    private final LoginServices loginServices;
    public LoginController(LoginServices loginServices) {
        this.loginServices = loginServices;
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Validated @RequestBody LoginDTO loginDTO, HttpServletResponse serve) {
        return loginServices.loginUser(loginDTO, serve);
    }
}
