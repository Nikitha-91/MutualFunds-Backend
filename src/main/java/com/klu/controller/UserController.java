package com.klu.controller;

import com.klu.dto.ApiResponse;
import com.klu.dto.UserResponseDTO;
import com.klu.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

@GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.ok("All users fetched", userService.getAllUsers()));
    }

@GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserById(id)));
    }

@GetMapping("/email")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserByEmail(email)));
    }

@GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(ApiResponse.ok("Users with role " + role, userService.getUsersByRole(role)));
    }

@PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateUser(
            @PathVariable Long id,
            @RequestBody UserService.UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User updated", userService.updateUser(id, request)));
    }

@PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> updateRole(
            @PathVariable Long id,
            @RequestBody RoleUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Role updated", userService.updateRole(id, request.role())));
    }

@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.ok("User deactivated", null));
    }

public record RoleUpdateRequest(String role) {}
}
