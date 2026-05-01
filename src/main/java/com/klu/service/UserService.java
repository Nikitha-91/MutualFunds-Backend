package com.klu.service;

import com.klu.dto.UserResponseDTO;
import com.klu.entity.User;
import com.klu.repository.UserRepository;
import com.klu.util.MappingUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

public record UserUpdateRequest(String name, String email, String password, String role) {}

@Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(MappingUtil::toUserResponse)
                .toList();
    }

@Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = findUserOrThrow(id);
        return MappingUtil.toUserResponse(user);
    }

@Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return MappingUtil.toUserResponse(user);
    }

@Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(String role) {
        User.Role userRole = User.Role.valueOf(role.toUpperCase());
        return userRepository.findByRole(userRole)
                .stream()
                .map(MappingUtil::toUserResponse)
                .toList();
    }

@Transactional
    public UserResponseDTO updateRole(Long id, String role) {
        User user = findUserOrThrow(id);
        user.setRole(User.Role.valueOf(role.toUpperCase()));
        return MappingUtil.toUserResponse(userRepository.save(user));
    }

@Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequest req) {
        User user = findUserOrThrow(id);

        if (req.name() != null && !req.name().isBlank()) {
            user.setName(req.name());
        }
        if (req.email() != null && !req.email().isBlank()) {
            user.setEmail(req.email());
        }
        if (req.password() != null && !req.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.password()));
        }
        if (req.role() != null && !req.role().isBlank()) {
            user.setRole(User.Role.valueOf(req.role().toUpperCase()));
        }

        return MappingUtil.toUserResponse(userRepository.save(user));
    }

@Transactional
    public void deleteUser(Long id) {
        User user = findUserOrThrow(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
