package com.reydiazz.bodeko.security.auth.service;

import com.reydiazz.bodeko.security.auth.web.request.LoginRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterOwnerRequest;
import com.reydiazz.bodeko.security.auth.web.response.AuthResponse;
import com.reydiazz.bodeko.security.auth.web.response.LoginResponse;
import com.reydiazz.bodeko.security.auth.web.response.RegisterResponse;

import java.util.UUID;

public interface AuthService {

    /** Inicio de sesión de propietarios en Bodeko. */
    LoginResponse login(LoginRequest request);

    RegisterResponse registerOwner(RegisterOwnerRequest request);

    /** Inicio de sesión de un cliente en una tienda concreta. */
    LoginResponse loginCustomer(UUID storeId, LoginRequest request);

    RegisterResponse registerCustomer(UUID storeId, RegisterCustomerRequest request);

    AuthResponse getAuthenticatedUser();

}
