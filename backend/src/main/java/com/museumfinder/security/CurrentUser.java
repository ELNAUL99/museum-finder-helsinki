package com.museumfinder.security;

import com.museumfinder.repo.FavoriteRepository;
import com.museumfinder.domain.Favorite;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/** Reads the authenticated visitor out of the security context, if there is one. */
@Component
public class CurrentUser {

    private final FavoriteRepository favorites;

    public CurrentUser(FavoriteRepository favorites) {
        this.favorites = favorites;
    }

    public Optional<AuthUser> get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthUser user) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public AuthUser require() {
        return get().orElseThrow(() -> new IllegalStateException("No authenticated user in context"));
    }

    /** Museum ids this visitor has starred; empty for anonymous visitors. */
    public Set<Long> favoriteIds() {
        return get()
                .map(user -> favorites.findByUserId(user.id()).stream()
                        .map(Favorite::getMuseumId)
                        .collect(Collectors.toSet()))
                .orElseGet(Set::of);
    }
}
