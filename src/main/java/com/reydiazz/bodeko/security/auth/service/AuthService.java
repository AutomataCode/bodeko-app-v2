package com.reydiazz.bodeko.security.auth.service;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.security.auth.web.request.LoginRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterOwnerRequest;
import com.reydiazz.bodeko.security.auth.web.response.AuthResponse;
import com.reydiazz.bodeko.security.auth.web.response.LoginResponse;
import com.reydiazz.bodeko.security.auth.web.response.RegisterResponse;


public interface AuthService {

    LoginResponse login(LoginRequest request);

    RegisterResponse registerCustomer(RegisterCustomerRequest request);

    RegisterResponse registerOwner(RegisterOwnerRequest request);

    AuthResponse getAuthenticatedUser();

    User findAuthenticatedUser();

}