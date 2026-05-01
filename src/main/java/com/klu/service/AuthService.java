package com.klu.service;

import com.klu.dto.AuthResponseDTO;
import com.klu.dto.LoginRequestDTO;
import com.klu.dto.RegisterRequestDTO;
import com.klu.entity.User;
import com.klu.exception.BadRequestException;
import com.klu.exception.DuplicateResourceException;
import com.klu.repository.UserRepository;
import com.klu.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository       userRepository;
    private final PasswordEncoder      passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil              jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

@Transactional
    public AuthResponseDTO register(RegisterRequestDTO req) {

if (isBlank(req.getName()))     throw new BadRequestException("Name is required.");
        if (isBlank(req.getEmail()))    throw new BadRequestException("Email is required.");
        if (isBlank(req.getPassword())) throw new BadRequestException("Password is required.");
        if (req.getPassword().length() < 6)
            throw new BadRequestException("Password must be at least 6 characters.");

if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("User", "email", req.getEmail());
        }

User.Role role = resolveRole(req.getRole());

User user = User.builder()
                .name(req.getName())
                .email(req.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(req.getPassword()))  
                .role(role)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        log.info("New user registered: email={}, role={}", user.getEmail(), user.getRole());

String token = jwtUtil.generateToken(buildSpringUser(user));

        return buildAuthResponse(token, user);
    }

public AuthResponseDTO login(LoginRequestDTO req) {

if (isBlank(req.getEmail()))    throw new BadRequestException("Email is required.");
        if (isBlank(req.getPassword())) throw new BadRequestException("Password is required.");

Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail().toLowerCase().trim(),
                        req.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        log.info("User logged in: email={}", req.getEmail());

User user = userRepository.findByEmail(req.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return buildAuthResponse(token, user);
    }

private User.Role resolveRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) return User.Role.INVESTOR;
        try {
            return User.Role.valueOf(roleStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return User.Role.INVESTOR;
        }
    }

private UserDetails buildSpringUser(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

private AuthResponseDTO buildAuthResponse(String token, User user) {
        return AuthResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .userId(user.getId())
                .build();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
