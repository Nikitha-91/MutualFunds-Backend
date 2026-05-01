package com.klu.dto;

public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean enabled;

    public UserResponseDTO() {}
    public UserResponseDTO(Long id, String name, String email, String role, boolean enabled) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String role;
        private boolean enabled;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder enabled(boolean enabled) { this.enabled = enabled; return this; }

        public UserResponseDTO build() {
            return new UserResponseDTO(id, name, email, role, enabled);
        }
    }
}
