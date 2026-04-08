package com.klu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Outbound DTO returned after successful authentication (register / login).
 *
 * Frontend usage:
 *   Store {@code token} and send as {@code Authorization: Bearer <token>}
 *   on all subsequent requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

    /** Signed JWT string */
    private String token;

    /** Always "Bearer" */
    @Builder.Default
    private String tokenType = "Bearer";

    /** User's full display name */
    private String name;

    /** User's email (also the JWT subject) */
    private String email;

    /** Granted role: ADMIN | INVESTOR | ADVISOR | ANALYST */
    private String role;

    /** Database primary key of the authenticated user */
    private Long userId;
}
