package com.klu.repository;

import com.klu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

boolean existsByEmail(String email);

List<User> findByRole(User.Role role);

List<User> findByEnabled(boolean enabled);
}
