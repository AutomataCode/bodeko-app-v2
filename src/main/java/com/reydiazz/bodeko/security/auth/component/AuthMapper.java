package com.reydiazz.bodeko.security.auth.component;

import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import com.reydiazz.bodeko.security.auth.web.response.AuthResponse;
import com.reydiazz.bodeko.security.auth.web.response.LoginResponse;
import com.reydiazz.bodeko.security.auth.web.response.RegisterResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthResponse toResponse(User user) {
        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getPhoneNumber(),
                null
        );
    }

    public AuthResponse toResponse(Customer customer) {
        return new AuthResponse(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                UserRole.CUSTOMER,
                customer.getPhoneNumber(),
                customer.getStoreId()
        );
    }

    public LoginResponse toLoginResponse(String token) {
        return new LoginResponse(token);
    }

    public RegisterResponse toRegisterResponse(String token) {
        return new RegisterResponse(token);
    }

}
