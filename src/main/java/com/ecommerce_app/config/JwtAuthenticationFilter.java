package com.ecommerce_app.config;

import com.ecommerce_app.exception.AppException;
import com.ecommerce_app.exception.ErrorCode;
import com.ecommerce_app.service.interfaces.AuthService;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;

    @Autowired
    public JwtAuthenticationFilter(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                SignedJWT signedJWT = authService.verifyToken(token);
                String jti = signedJWT.getJWTClaimsSet().getJWTID();

                if (authService.isTokenInvalidated(jti)) {
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                }

            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        }

        filterChain.doFilter(request, response);
    }
}

