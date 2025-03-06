package com.example.aggregadordeinvestimentos.controller.dto;

public record LoginResponse(String accessToken, String id, String username, String email, Long expiresIn) {
}
