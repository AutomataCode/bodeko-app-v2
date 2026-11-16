package com.reydiazz.bodeko.security.config;

import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/** Cliente autenticado en una sola tienda. */
public record CustomerPrincipal(Customer customer) implements UserDetails {

    @Override
    @NonNull
    public String getUsername() {
        return customer.getId().toString();
    }

    @Override
    @NonNull
    public String getPassword() {
        return customer.getPassword();
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + UserRole.CUSTOMER.name()));
    }

    @Override
    public boolean isEnabled() {
        return customer.isActive();
    }

}
