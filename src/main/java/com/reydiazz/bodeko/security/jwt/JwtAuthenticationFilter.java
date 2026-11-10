package com.reydiazz.bodeko.security.jwt;

import com.reydiazz.bodeko.security.auth.util.AuthRoutes;
import com.reydiazz.bodeko.security.config.CustomUserDetailsService;
import com.reydiazz.bodeko.security.util.HttpResponse;
import com.reydiazz.bodeko.shared.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals(AuthRoutes.API_BASE + AuthRoutes.LOGIN)
                || path.equals(AuthRoutes.API_BASE + AuthRoutes.REGISTER_CUSTOMER)
                || path.equals(AuthRoutes.API_BASE + AuthRoutes.REGISTER_OWNER);
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain chain
    ) throws ServletException, IOException {
        String token = extractToken(request);
        if (token != null) {
            try {
                authenticateUserIfValid(token);
            } catch (ExpiredJwtException exception) {
                ErrorResponse error = ErrorResponse.builder()
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .code("TOKEN_EXPIRED")
                        .message("Authentication token has expired.")
                        .build();
                HttpResponse.sendError(error, response, objectMapper);
                return;
            } catch (JwtException | IllegalArgumentException | UsernameNotFoundException exception) {
                ErrorResponse error = ErrorResponse.builder()
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .code("INVALID_TOKEN")
                        .message("Authentication token is invalid.")
                        .build();
                HttpResponse.sendError(error, response, objectMapper);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) return null;
        return authHeader.substring(BEARER_PREFIX.length()).trim();
    }

    private void authenticateUserIfValid(String token) {
        String username = jwtService.extractUsername(token);
        if (shouldSkipAuthentication(username)) return;
        UserDetails user = customUserDetailsService.loadUserByUsername(username);
        if (!jwtService.isTokenValid(user, token)) return;
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private boolean shouldSkipAuthentication(String username) {
        return username == null || SecurityContextHolder.getContext().getAuthentication() != null;
    }

}