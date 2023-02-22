package com.example.pizzaproject.auth;

public class JwtResponse {
    private String jwttoken;
    private String status;
    private String refreshToken;

    public JwtResponse(String status, String jwttoken, String RefreshToken) {
        this.status = status;
        this.jwttoken = jwttoken;
        this.refreshToken = RefreshToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJwttoken() {
        return jwttoken;
    }

    public void setJwttoken(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
