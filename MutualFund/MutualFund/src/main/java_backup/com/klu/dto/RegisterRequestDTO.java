package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Inbound DTO for user registration.
 *
 * Maps to {@code User} entity fields:
 *   name | email | password | role
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    /** Full display name, e.g. "John Smith" */
    private String name;

    /** Email address — used as login credential */
    private String email;

    /** Plain-text password (will be BCrypt-hashed before storage) */
    private String password;

    /** Optional role: ADMIN | INVESTOR | ADVISOR | ANALYST (defaults to INVESTOR) */
    private String role;
}
