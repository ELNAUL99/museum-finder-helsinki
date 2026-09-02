package com.museumfinder.security;

/** The authenticated principal placed in the security context by {@link JwtAuthenticationFilter}. */
public record AuthUser(Long id, String email, String displayName) {
}
