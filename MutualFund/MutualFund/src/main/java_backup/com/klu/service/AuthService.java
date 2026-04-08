package com.klu.service;

import com.klu.dto.AuthResponseDTO;
import com.klu.dto.LoginRequestDTO;
import com.klu.dto.RegisterRequestDTO;
import com.klu.entity.User;
import com.klu.exception.BadRequestException;
import com.klu.exception.DuplicateResourceException;
import com.klu.repository.UserRepository;
import com.klu.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles user registration and JWT-based login.
 *
 * <p>Login credential  : email + password
 * <p>Password storage  : BCrypt-hashed via Spring's {@link PasswordEncoder}
 * <p>Token type        : JWT (HMAC-SHA256), valid for 24 h by default
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository       userRepository;
    private final PasswordEncoder      passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil              jwtUtil;

    // ================================================================
    //  REGISTER
    // ================================================================

    /**
     * Creates a new user account and returns a ready-to-use JWT.
     *
     * <p>Steps:
     * <ol>
     *   <li>Validate request fields (not blank)</li>
     *   <li>Check email uniqueness</li>
     *   <li>Hash the password with BCrypt</li>
     *   <li>Persist the {@link User}</li>
     *   <li>Generate and return a JWT</li>
     * </ol>
     *
     * @param req registration payload
     * @return {@link AuthResponseDTO} containing the JWT token
     * @throws BadRequestException        if required fields are blank
     * @throws DuplicateResourceException if the email is already registered
     */
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO req) {

        // ── Input validation ─────────────────────────────────────────
        if (isBlank(req.getName()))     throw new BadRequestException("Name is required.");
        if (isBlank(req.getEmail()))    throw new BadRequestException("Email is required.");
        if (isBlank(req.getPassword())) throw new BadRequestException("Password is required.");
        if (req.getPassword().length() < 6)
            throw new BadRequestException("Password must be at least 6 characters.");

        // ── Email uniqueness check ────────────────────────────────────
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException("User", "email", req.getEmail());
        }

        // ── Resolve role (defaults to INVESTOR if omitted / invalid) ──
        User.Role role = resolveRole(req.getRole());

        // ── Persist user ──────────────────────────────────────────────
        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail().toLowerCase().trim())
                .password(passwordEncoder.encode(req.getPassword()))  // BCrypt hash
                .role(role)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        log.info("New user registered: email={}, role={}", user.getEmail(), user.getRole());

        // ── Generate JWT ──────────────────────────────────────────────
        String token = jwtUtil.generateToken(buildSpringUser(user));

        return buildAuthResponse(token, user);
    }

    // ================================================================
    //  LOGIN
    // ================================================================

    /**
     * Authenticates a user by email + password and returns a JWT.
     *
     * <p>Delegates credential verification to Spring Security's
     * {@link AuthenticationManager} (which uses BCrypt internally).
     *
     * @param req login payload
     * @return {@link AuthResponseDTO} containing the JWT token
     * @throws BadCredentialsException if credentials are wrong
     */
    public AuthResponseDTO login(LoginRequestDTO req) {

        // ── Input validation ─────────────────────────────────────────
        if (isBlank(req.getEmail()))    throw new BadRequestException("Email is required.");
        if (isBlank(req.getPassword())) throw new BadRequestException("Password is required.");

        // ── Authenticate (throws BadCredentialsException on failure) ──
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail().toLowerCase().trim(),
                        req.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        log.info("User logged in: email={}", req.getEmail());

        // Fetch entity to build full response
        User user = userRepository.findByEmail(req.getEmail().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return buildAuthResponse(token, user);
    }

    // ================================================================
    //  PRIVATE HELPERS
    // ================================================================

    /** Map role string to enum; defaults to INVESTOR if null / invalid. */
    private User.Role resolveRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) return User.Role.INVESTOR;
        try {
            return User.Role.valueOf(roleStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return User.Role.INVESTOR;
        }
    }

    /** Build a Spring Security UserDetails from a persisted User. */
    private UserDetails buildSpringUser(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

    /** Assemble the AuthResponseDTO. */
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
