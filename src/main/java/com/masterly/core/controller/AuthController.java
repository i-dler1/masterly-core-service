package com.masterly.core.controller;

import com.masterly.core.dto.LoginRequest;
import com.masterly.core.dto.LoginResponse;
import com.masterly.core.dto.MasterDto;
import com.masterly.core.entity.Master;
import com.masterly.core.repository.MasterRepository;
import com.masterly.core.security.JwtUtil;
import com.masterly.core.service.MasterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для аутентификации пользователей.
 * Предоставляет API для входа в систему и регистрации.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MasterService masterService;
    private final MasterRepository masterRepository;

    /**
     * Аутентификация пользователя.
     *
     * @param loginRequest DTO с email и паролем
     * @return JWT токен при успешной аутентификации
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for user: {}", loginRequest.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Authentication successful for user: {}", loginRequest.getEmail());

        // Безопасное извлечение UserDetails
        UserDetails userDetails;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            userDetails = (UserDetails) principal;
        } else {
            log.error("Authentication principal is not UserDetails: {}", principal);
            throw new IllegalStateException("Invalid authentication principal type");
        }

        String jwt = jwtUtil.generateToken(userDetails);
        log.debug("JWT generated for user: {}", loginRequest.getEmail());

        MasterDto master = masterService.findByEmail(loginRequest.getEmail());

        log.info("User logged in successfully: {}", loginRequest.getEmail());

        return ResponseEntity.ok(LoginResponse.builder()
                .token(jwt)
                .id(master.getId())
                .email(master.getEmail())
                .name(master.getFullName())
                .role(master.getRole())
                .type("Bearer")
                .build());
    }

    @PostMapping("/test-password")
    public ResponseEntity<?> testPassword(@RequestParam String email, @RequestParam String rawPassword) {
        Master master = masterRepository.findByEmail(email).orElse(null);
        if (master == null) {
            return ResponseEntity.ok("User not found");
        }
        boolean matches = new BCryptPasswordEncoder().matches(rawPassword, master.getPasswordHash());
        return ResponseEntity.ok("Password matches: " + matches);
    }
}