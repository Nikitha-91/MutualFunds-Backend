package com.klu.dto;

public class AuthResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private String name;
    private String email;
    private String role;
    private Long userId;

    public AuthResponseDTO() {}

    public AuthResponseDTO(String token, String tokenType, String name,
                           String email, String role, Long userId) {
        this.token = token;
        this.tokenType = tokenType;
        this.name = name;
        this.email = email;
        this.role = role;
        this.userId = userId;
    }

public String getToken() { return token; }
    public String getTokenType() { return tokenType; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public Long getUserId() { return userId; }

public void setToken(String token) { this.token = token; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setUserId(Long userId) { this.userId = userId; }

public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String token;
        private String tokenType = "Bearer";
        private String name;
        private String email;
        private String role;
        private Long userId;

        public Builder token(String token) { this.token = token; return this; }
        public Builder tokenType(String tokenType) { this.tokenType = tokenType; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder role(String role) { this.role = role; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }

        public AuthResponseDTO build() {
            return new AuthResponseDTO(token, tokenType, name, email, role, userId);
        }
    }
}
