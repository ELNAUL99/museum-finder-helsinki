package com.museumfinder.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param secret       HMAC signing secret; must be at least 32 characters
 * @param ttlMinutes   token lifetime
 */
@ConfigurationProperties(prefix = "museumfinder.jwt")
public record JwtProperties(String secret, long ttlMinutes) {

    public JwtProperties {
        if (ttlMinutes <= 0) {
            ttlMinutes = 60 * 24 * 7;
        }
    }
}
