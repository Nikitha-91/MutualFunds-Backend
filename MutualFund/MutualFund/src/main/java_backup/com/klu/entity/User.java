package com.klu.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a registered user of the Mutual Fund platform.
 *
 * Fields
 * ------
 * id       – auto-generated primary key
 * name     – full display name of the user
 * email    – unique email address (used for contact / lookup)
 * password – BCrypt-hashed password (never returned to client)
 * role     – one of ADMIN | INVESTOR | ADVISOR | ANALYST
 * enabled  – soft-disable flag (false = account deactivated)
 */
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Full display name, e.g. "John Smith" */
    @Column(nullable = false, length = 100)
    private String name;

    /** Unique email – used as login username by Spring Security */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** BCrypt-hashed password – never expose in responses */
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    // ---- Role enum ----
    public enum Role {
        ADMIN,
        INVESTOR,
        ADVISOR,
        ANALYST
    }
}
