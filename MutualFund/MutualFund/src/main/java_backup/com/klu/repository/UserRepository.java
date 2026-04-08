package com.klu.repository;

import com.klu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for {@link User} entities.
 *
 * Login credential = email (unique).
 * Name is a display-only field and need not be unique.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ---- Lookup ----
    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    // ---- Existence checks (used in registration) ----
    boolean existsByEmail(String email);

    // ---- Filter by role ----
    List<User> findByRole(User.Role role);

    // ---- Find all active / disabled ----
    List<User> findByEnabled(boolean enabled);
}
