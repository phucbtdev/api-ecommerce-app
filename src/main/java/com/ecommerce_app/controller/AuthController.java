package com.ecommerce_app.controller;

import com.ecommerce_app.dto.auth.LoginRequest;
import com.ecommerce_app.dto.auth.SignupRequest;
import com.ecommerce_app.dto.auth.TokenResponse;
import com.ecommerce_app.dto.request.ForgotPasswordRequest;
import com.ecommerce_app.dto.request.RefreshTokenRequest;
import com.ecommerce_app.dto.request.ResetPasswordRequest;
import com.ecommerce_app.dto.response.ApiResult;
import com.ecommerce_app.dto.response.AuthenticationResponse;
import com.ecommerce_app.dto.response.UserResponse;
import com.ecommerce_app.service.interfaces.AuthService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

/**
 * AuthController handles authentication-related operations such as login, signup,
 * password reset, and token refresh.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints related to user authentication")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user and returns JWT access and refresh tokens.
     *
     * @param loginRequest contains the user's login credentials (email and password)
     * @return ApiResult with the authentication tokens if login is successful
     */
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ApiResult<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = authService.login(loginRequest);
        return ApiResult.success("Login successfully!", tokenResponse);
    }

    /**
     * Registers a new user account.
     *
     * @param signupRequest contains the new user's signup details
     * @return ApiResult with the created user's information
     */
    @Operation(summary = "User signup", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signup successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/signup")
    public ApiResult<UserResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        UserResponse userResponse = authService.signup(signupRequest);
        return ApiResult.success("Sign up successfully!", userResponse);
    }

    /**
     * Sends a password reset email to the user's registered email address.
     *
     * @param forgotPasswordRequest contains the email to send the reset link to
     * @return ApiResult indicating the email was sent successfully
     */
    @Operation(summary = "Forgot password", description = "Send password reset email to user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset email sent")
    })
    @PostMapping("/forgot-password")
    public ApiResult<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        authService.forgotPassword(forgotPasswordRequest);
        return ApiResult.success("Password reset email sent successfully", null);
    }

    /**
     * Resets the user's password using a valid reset token.
     *
     * @param resetPasswordRequest contains the new password and reset token
     * @return ApiResult indicating the password was reset successfully
     */
    @Operation(summary = "Reset password", description = "Reset password using token received via email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successful"),
            @ApiResponse(responseCode = "400", description = "Invalid token or request")
    })
    @PostMapping("/reset-password")
    public ApiResult<String> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        authService.resetPassword(resetPasswordRequest);
        return ApiResult.success("Password reset successfully", null);
    }

    /**
     * Refreshes the JWT access token using a valid refresh token.
     *
     * @param refreshTokenRequest contains the current refresh token
     * @return ApiResult with new access and refresh tokens
     * @throws ParseException if the refresh token is malformed
     * @throws JOSEException if there is an issue parsing or verifying the JWT
     */
    @Operation(summary = "Refresh token", description = "Obtain a new JWT access token using a refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh-token")
    public ApiResult<AuthenticationResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ) throws ParseException, JOSEException {
        AuthenticationResponse response = authService.refreshToken(refreshTokenRequest);
        return ApiResult.success("Token refreshed successfully", response);
    }
}
