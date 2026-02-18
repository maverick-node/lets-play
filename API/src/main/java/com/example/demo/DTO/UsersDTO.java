package com.example.demo.DTO;

public class UsersDTO {
    private String username;
    private String email;
    private String role;
    private String userUuid;

    // Constructors
    public UsersDTO() {}

    public UsersDTO(String username, String email, String role, String userUuid) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.userUuid = userUuid;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUserUuid() { return userUuid; }
    public void setUserUuid(String userUuid) { this.userUuid = userUuid; }
}
