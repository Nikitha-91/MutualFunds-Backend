package com.klu.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false, length = 100)
    private String name;

@Column(nullable = false, unique = true, length = 100)
    private String email;

@Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true;

public enum Role {
        ADMIN,
        INVESTOR,
        ADVISOR,
        ANALYST
    }

public User() {}

public User(Long id, String name, String email, String password, Role role, boolean enabled) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
    }

public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }

public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private Role role;
        private boolean enabled = true;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }

        public User build() {
            return new User(id, name, email, password, role, enabled);
        }
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + email + "', role=" + role + ", enabled=" + enabled + "}";
    }
}
