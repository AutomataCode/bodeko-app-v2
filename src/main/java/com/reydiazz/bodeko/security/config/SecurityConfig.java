package com.reydiazz.bodeko.security.config;

import com.reydiazz.bodeko.core.store.util.StoreRouters;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import com.reydiazz.bodeko.security.auth.util.AuthRoutes;
import com.reydiazz.bodeko.security.authorization.component.StoreOwnerAuthorizationManager;
import com.reydiazz.bodeko.security.authorization.util.CustomerRoutes;
import com.reydiazz.bodeko.security.authorization.util.PublicRoutes;
import com.reydiazz.bodeko.security.authorization.util.StoreScopeRoutes;
import com.reydiazz.bodeko.security.jwt.JwtAuthenticationFilter;
import com.reydiazz.bodeko.security.util.HttpResponse;
import com.reydiazz.bodeko.shared.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.frontend-url}")
    private String FRONTEND_URL;

    private final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
    private final List<String> EXPOSED_HEADERS = List.of("Authorization");
    private final String API_PATTERN = "/**";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final StoreOwnerAuthorizationManager storeOwnerAuthorizationManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(FRONTEND_URL));
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(EXPOSED_HEADERS);
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(API_PATTERN, config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, API_PATTERN).permitAll()
                        .requestMatchers(
                                AuthRoutes.API_BASE + AuthRoutes.LOGIN,
                                AuthRoutes.API_BASE + AuthRoutes.REGISTER_CUSTOMER,
                                AuthRoutes.API_BASE + AuthRoutes.REGISTER_OWNER)
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, PublicRoutes.PATTERN).permitAll()
                        .requestMatchers(CustomerRoutes.PATTERN).hasRole(UserRole.CUSTOMER.name())
                        .requestMatchers(StoreRouters.API_BASE).hasRole(UserRole.ADMIN.name())
                        .requestMatchers(StoreScopeRoutes.PATTERN).access(storeOwnerAuthorizationManager)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(this::sendUnauthorizedResponse)
                        .accessDeniedHandler(this::sendForbiddenResponse)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void sendUnauthorizedResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .code("UNAUTHORIZED")
                .message("Authentication is required to access this resource.")
                .build();
        HttpResponse.sendError(error, response, objectMapper);
    }

    private void sendForbiddenResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception) throws IOException {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpServletResponse.SC_FORBIDDEN)
                .code("ACCESS_DENIED")
                .message("You do not have permission to perform this operation.")
                .build();
        HttpResponse.sendError(error, response, objectMapper);
    }

}