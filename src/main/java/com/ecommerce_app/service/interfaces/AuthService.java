package com.ecommerce_app.service.interfaces;

import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import com.ecommerce_app.dto.request.ForgotPasswordRequest;
import com.ecommerce_app.dto.request.LogoutRequest;
import com.ecommerce_app.dto.request.RefreshTokenRequest;
import com.ecommerce_app.dto.request.ResetPasswordRequest;
import com.ecommerce_app.dto.response.AuthenticationResponse;
import com.ecommerce_app.dto.response.UserResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthService {
    TokenResponse login(LoginRequest loginRequest);
    UserResponse signup(SignupRequest signupRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;
    SignedJWT verifyToken(String token) throws JOSEException, ParseException;
    boolean isTokenInvalidated(String token);
    void logout(LogoutRequest request) throws ParseException, JOSEException;
}