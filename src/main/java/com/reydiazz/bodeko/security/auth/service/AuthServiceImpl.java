package com.reydiazz.bodeko.security.auth.service;

import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.core.customer.repository.CustomerRepository;
import com.reydiazz.bodeko.core.customer.service.CustomerService;
import com.reydiazz.bodeko.core.user.core.service.UserService;
import com.reydiazz.bodeko.security.auth.component.AuthMapper;
import com.reydiazz.bodeko.security.auth.web.request.LoginRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterOwnerRequest;
import com.reydiazz.bodeko.security.auth.web.response.AuthResponse;
import com.reydiazz.bodeko.security.auth.web.response.LoginResponse;
import com.reydiazz.bodeko.security.auth.web.response.RegisterResponse;
import com.reydiazz.bodeko.security.config.CustomerPrincipal;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import com.reydiazz.bodeko.security.jwt.JwtService;
import com.reydiazz.bodeko.security.util.SecurityContextFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthMapper authMapper;
    private final JwtService jwtService;
    private final SecurityContextFacade securityContext;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    /** Hash de relleno: si el correo no existe se compara igual, para no revelarlo por el tiempo de respuesta. */
    private volatile String dummyHash;

    @Override
    public RegisterResponse registerOwner(RegisterOwnerRequest request) {
        userService.createOwnerEntity(request);
        return authMapper.toRegisterResponse(authenticateOwner(request.email(), request.password()));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return authMapper.toLoginResponse(authenticateOwner(request.email(), request.password()));
    }

    @Override
    public RegisterResponse registerCustomer(UUID storeId, RegisterCustomerRequest request) {
        Customer customer = customerService.create(storeId, request);
        return authMapper.toRegisterResponse(jwtService.generateCustomerToken(customer.getId(), storeId));
    }

    @Override
    public LoginResponse loginCustomer(UUID storeId, LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        Optional<Customer> found = customerRepository.findByStoreAndEmail(storeId, email);
        String hash = found.map(Customer::getPassword).orElseGet(this::dummyHash);
        boolean matches = passwordEncoder.matches(request.password(), hash);
        if (found.isEmpty() || !matches) throw new BadCredentialsException("BAD_CREDENTIALS");
        Customer customer = found.get();
        if (!customer.isActive()) throw new DisabledException("ACCOUNT_DISABLED");
        return authMapper.toLoginResponse(jwtService.generateCustomerToken(customer.getId(), storeId));
    }

    @Override
    public AuthResponse getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth == null ? null : auth.getPrincipal();
        if (principal instanceof UserPrincipal owner) return authMapper.toResponse(owner.user());
        if (principal instanceof CustomerPrincipal customer) return authMapper.toResponse(customer.customer());
        throw new AuthenticationCredentialsNotFoundException("AUTHENTICATION_REQUIRED");
    }

    private String authenticateOwner(String email, String password) {
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(normalizedEmail, password);
        Authentication auth = authenticationManager.authenticate(authToken);
        UserPrincipal userPrincipal = securityContext.extractPrincipal(auth);
        return jwtService.generateOwnerToken(userPrincipal.user().getId());
    }

    private String dummyHash() {
        if (dummyHash == null) dummyHash = passwordEncoder.encode(UUID.randomUUID().toString());
        return dummyHash;
    }

}
