package com.ecommerce_app.controller;

import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
//    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest
    ) {

//        TokenResponse tokenResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(TokenResponse.builder().build());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody SignupRequest signUpRequest
    ) {

//        User user = authService.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully with ID: " );
    }
}
