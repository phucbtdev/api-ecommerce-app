package com.ecommerce_app.controller;

import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import com.ecommerce_app.dto.request.ForgotPasswordRequest;
import com.ecommerce_app.dto.request.RefreshTokenRequest;
import com.ecommerce_app.dto.request.ResetPasswordRequest;
import com.ecommerce_app.dto.response.ApiResponse;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.service.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return ApiResponse.<UserResponse>builder()
                .message("Sign up successfully!")
                .data(authService.signup(signupRequest))
                .build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Password reset email sent successfully");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse tokenResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}