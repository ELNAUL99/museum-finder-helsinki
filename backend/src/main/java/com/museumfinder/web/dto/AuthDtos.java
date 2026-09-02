package com.museumfinder.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record RegisterRequest(
            @Email @NotBlank String email,
            @NotBlank @Size(min = 8, max = 100, message = "Password must be at least 8 characters") String password,
            @NotBlank @Size(max = 80) String displayName) {
    }

    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {
    }

    public record UserDto(Long id, String email, String displayName) {
    }

    public record AuthResponse(String token, long expiresInSeconds, UserDto user) {
    }
}
