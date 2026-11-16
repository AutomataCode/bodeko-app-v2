package com.reydiazz.bodeko.security.jwt;

import com.reydiazz.bodeko.security.auth.util.AuthRoutes;
import com.reydiazz.bodeko.security.config.CustomUserDetailsService;
import com.reydiazz.bodeko.security.config.CustomerPrincipal;
import com.reydiazz.bodeko.security.util.HttpResponse;
import com.reydiazz.bodeko.shared.exception.ErrorResponse;
import io.jsonwebtoken.Claims;
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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final List<String> OPEN_PATHS = List.of(
            AuthRoutes.API_BASE + AuthRoutes.LOGIN,
            AuthRoutes.API_BASE + AuthRoutes.REGISTER_OWNER,
            AuthRoutes.STORE_LOGIN_PATTERN,
            AuthRoutes.STORE_REGISTER_PATTERN);

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return OPEN_PATHS.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
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
            } catch (JwtException | IllegalArgumentException | NullPointerException | UsernameNotFoundException exception) {
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

    /** El claim "kind" decide si la cuenta es un propietario (users) o un cliente de tienda (customers). */
    private void authenticateUserIfValid(String token) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) return;
        Claims claims = jwtService.parse(token);
        UUID id = UUID.fromString(claims.getSubject());
        String kind = claims.get(JwtService.KIND_CLAIM, String.class);
        UserDetails principal;
        if (JwtService.KIND_CUSTOMER.equals(kind)) {
            CustomerPrincipal customer = customUserDetailsService.loadCustomerById(id);
            UUID tokenStore = UUID.fromString(claims.get(JwtService.STORE_CLAIM, String.class));
            if (!tokenStore.equals(customer.customer().getStoreId())) {
                throw new UsernameNotFoundException("STORE_MISMATCH");
            }
            principal = customer;
        } else if (JwtService.KIND_OWNER.equals(kind)) {
            principal = customUserDetailsService.loadOwnerById(id);
        } else {
            throw new IllegalArgumentException("Unknown token kind");
        }
        if (!principal.isEnabled()) return;
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
