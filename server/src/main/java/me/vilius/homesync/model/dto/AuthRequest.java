package me.vilius.homesync.model.dto;

public class AuthRequest {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private String username;
    private String password;
}