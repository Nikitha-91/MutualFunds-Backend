package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.AuthResponseDTO;
import com.klu.dto.LoginRequestDTO;
import com.klu.dto.RegisterRequestDTO;
import com.klu.service.AuthService;
import com.klu.util.SecurityUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(
            @RequestBody RegisterRequestDTO request) {

        AuthResponseDTO response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Account created successfully", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
            @RequestBody LoginRequestDTO request) {

        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<String>> me() {
        String email = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(ApiResponse.ok("Current principal", email));
    }
}
