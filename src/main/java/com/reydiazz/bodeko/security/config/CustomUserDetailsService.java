package com.reydiazz.bodeko.security.config;

import com.reydiazz.bodeko.core.customer.repository.CustomerRepository;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.core.user.core.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** Carga propietarios (tabla users) y clientes de tienda (tabla customers). */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    /** Usado por el inicio de sesión de propietarios en Bodeko. */
    @Override
    @NonNull
    public UserDetails loadUserByUsername(@NonNull String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("USER_NOT_FOUND")
        );
        return new UserPrincipal(user);
    }

    public UserPrincipal loadOwnerById(UUID id) {
        return userRepository.findById(id).map(UserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
    }

    public CustomerPrincipal loadCustomerById(UUID id) {
        return customerRepository.findById(id).map(CustomerPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
    }

}
