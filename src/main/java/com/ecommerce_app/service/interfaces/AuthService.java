/**
 * Service interface for handling authentication and authorization operations in the e-commerce application.
 * Provides functionality for user login, signup, password management, and token handling.
 */
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
    /**
     * Authenticates a user based on credentials and returns a token if successful.
     *
     * @param loginRequest The DTO containing login credentials
     * @return A token response containing authentication tokens
     */
    TokenResponse login(LoginRequest loginRequest);

    /**
     * Registers a new user in the system.
     *
     * @param signupRequest The DTO containing user registration details
     * @return The newly created user as a UserResponse
     */
    UserResponse signup(SignupRequest signupRequest);

    /**
     * Initiates the password recovery process for a user.
     *
     * @param forgotPasswordRequest The DTO containing information to identify the user
     */
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

    /**
     * Completes the password reset process with a new password.
     *
     * @param resetPasswordRequest The DTO containing the reset token and new password
     */
    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    /**
     * Renews the authentication token using a refresh token.
     *
     * @param refreshTokenRequest The DTO containing the refresh token
     * @return A new authentication response with fresh tokens
     * @throws ParseException If token parsing fails
     * @throws JOSEException If there are JOSE-related issues
     */
    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    /**
     * Verifies the validity of an authentication token.
     *
     * @param token The JWT token string to verify
     * @return The verified SignedJWT object
     * @throws JOSEException If there are JOSE-related issues
     * @throws ParseException If token parsing fails
     */
    SignedJWT verifyToken(String token) throws JOSEException, ParseException;

    /**
     * Checks if a token has been invalidated (e.g., due to logout).
     *
     * @param token The JWT token string to check
     * @return true if the token is invalidated, false otherwise
     */
    boolean isTokenInvalidated(String token);

    /**
     * Logs out a user by invalidating their current token.
     *
     * @param request The DTO containing logout information
     * @throws ParseException If token parsing fails
     * @throws JOSEException If there are JOSE-related issues
     */
    void logout(LogoutRequest request) throws ParseException, JOSEException;
}