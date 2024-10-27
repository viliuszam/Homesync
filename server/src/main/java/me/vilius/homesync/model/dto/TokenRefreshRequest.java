package me.vilius.homesync.model.dto;

public class TokenRefreshRequest {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenRefreshRequest() {
    }

    public TokenRefreshRequest(String token) {
        this.token = token;
    }

    private String token;
}