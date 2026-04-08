package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Outbound DTO representing a User.
 * Never exposes the password field.
 *
 * Fields match the updated {@code User} entity:
 *   id | name | email | role | enabled
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;

    /** Full display name (e.g. "John Smith") */
    private String name;

    /** Email address – the unique login credential */
    private String email;

    /** Role: ADMIN | INVESTOR | ADVISOR | ANALYST */
    private String role;

    /** false = account deactivated */
    private boolean enabled;
}
