package com.museumfinder.web;

import com.museumfinder.domain.AppUser;
import com.museumfinder.error.ConflictException;
import com.museumfinder.repo.UserRepository;
import com.museumfinder.security.AuthUser;
import com.museumfinder.security.CurrentUser;
import com.museumfinder.security.JwtService;
import com.museumfinder.web.dto.AuthDtos;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CurrentUser currentUser;

    public AuthController(UserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService,
                          CurrentUser currentUser) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.currentUser = currentUser;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDtos.AuthResponse register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        if (users.existsByEmailIgnoreCase(request.email())) {
            throw new ConflictException("That email address is already registered.");
        }
        AppUser user = new AppUser();
        user.setEmail(request.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName());
        users.save(user);
        return toResponse(user);
    }

    @PostMapping("/login")
    public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        AppUser user = users.findByEmailIgnoreCase(request.email())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(() -> new BadCredentialsException("bad credentials"));
        return toResponse(user);
    }

    @GetMapping("/me")
    public AuthDtos.UserDto me() {
        AuthUser user = currentUser.require();
        return new AuthDtos.UserDto(user.id(), user.email(), user.displayName());
    }

    private AuthDtos.AuthResponse toResponse(AppUser user) {
        return new AuthDtos.AuthResponse(
                jwtService.issue(user),
                jwtService.ttlSeconds(),
                new AuthDtos.UserDto(user.getId(), user.getEmail(), user.getDisplayName()));
    }
}
