package com.reydiazz.bodeko.core.user.core.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.user.core.exception.UserEmailExistException;
import com.reydiazz.bodeko.core.user.core.exception.UserErrorCode;
import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import com.reydiazz.bodeko.core.user.core.repository.UserRepository;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterOwnerRequest;
import com.reydiazz.bodeko.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createCustomerEntity(RegisterCustomerRequest request) {
        String email = normalizeEmail(request.email());
        validateEmailIsAvailable(email);
        String passwordHash = passwordEncoder.encode(request.password());
        User user = User.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .email(email)
                .password(passwordHash)
                .fullName(request.fullName())
                .role(UserRole.CUSTOMER)
                .phoneNumber(request.phoneNumber())
                .documentType(request.documentType())
                .documentNumber(request.documentType() == null ? null : request.documentNumber())
                .createdAt(OffsetDateTime.now())
                .build();
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User createOwnerEntity(RegisterOwnerRequest request) {
        String email = normalizeEmail(request.email());
        validateEmailIsAvailable(email);
        String passwordHash = passwordEncoder.encode(request.password());
        User user = User.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .email(email)
                .password(passwordHash)
                .fullName(request.fullName())
                .role(UserRole.ADMIN)
                .phoneNumber(request.phoneNumber())
                .createdAt(OffsetDateTime.now())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User getReferenceById(UUID id) {
        return userRepository.getReferenceById(id);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private void validateEmailIsAvailable(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserEmailExistException();
        }
    }

}
