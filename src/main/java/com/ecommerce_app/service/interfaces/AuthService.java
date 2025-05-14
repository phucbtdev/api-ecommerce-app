package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import com.ecommerce_app.dto.request.ForgotPasswordRequest;
import com.ecommerce_app.dto.request.RefreshTokenRequest;
import com.ecommerce_app.dto.request.ResetPasswordRequest;
import com.ecommerce_app.dto.response.UserResponse;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);
    UserResponse signup(SignupRequest signupRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}