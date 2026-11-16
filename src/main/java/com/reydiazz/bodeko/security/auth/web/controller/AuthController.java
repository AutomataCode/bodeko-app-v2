package com.reydiazz.bodeko.security.auth.web.controller;


import com.reydiazz.bodeko.security.auth.service.AuthService;
import com.reydiazz.bodeko.security.auth.util.AuthRoutes;
import com.reydiazz.bodeko.security.auth.web.request.LoginRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterOwnerRequest;
import com.reydiazz.bodeko.security.auth.web.response.AuthResponse;
import com.reydiazz.bodeko.security.auth.web.response.LoginResponse;
import com.reydiazz.bodeko.security.auth.web.response.RegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(AuthRoutes.API_BASE)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(AuthRoutes.LOGIN)
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(AuthRoutes.REGISTER_OWNER)
    public ResponseEntity<RegisterResponse> registerOwner(@Valid @RequestBody RegisterOwnerRequest request) {
        RegisterResponse response = authService.registerOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(AuthRoutes.STORE_LOGIN)
    public ResponseEntity<LoginResponse> loginCustomer(
            @PathVariable UUID storeId, @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.loginCustomer(storeId, request));
    }

    @PostMapping(AuthRoutes.STORE_REGISTER)
    public ResponseEntity<RegisterResponse> registerCustomer(
            @PathVariable UUID storeId, @Valid @RequestBody RegisterCustomerRequest request) {
        RegisterResponse response = authService.registerCustomer(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(AuthRoutes.ME)
    public ResponseEntity<AuthResponse> me() {
        AuthResponse response = authService.getAuthenticatedUser();
        return ResponseEntity.ok(response);
    }

}
