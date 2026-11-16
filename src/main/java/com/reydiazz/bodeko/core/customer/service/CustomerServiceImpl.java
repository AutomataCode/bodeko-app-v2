package com.reydiazz.bodeko.core.customer.service;

import com.github.f4b6a3.uuid.UuidCreator;
import com.reydiazz.bodeko.core.customer.exception.CustomerEmailExistException;
import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.core.customer.repository.CustomerRepository;
import com.reydiazz.bodeko.core.store.exception.StoreNotFoundException;
import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Customer create(UUID storeId, RegisterCustomerRequest request) {
        if (!storeRepository.existsById(storeId)) throw new StoreNotFoundException();
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        if (customerRepository.existsByStoreAndEmail(storeId, email)) {
            throw new CustomerEmailExistException();
        }
        Customer customer = Customer.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .store(storeRepository.getReferenceById(storeId))
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .documentType(request.documentType())
                .documentNumber(request.documentType() == null ? null : request.documentNumber())
                .createdAt(OffsetDateTime.now())
                .marketingConsent(request.marketingConsent())
                .build();
        try {
            return customerRepository.saveAndFlush(customer);
        } catch (DataIntegrityViolationException exception) {
            // Dos registros simultáneos con el mismo correo: el índice único decide.
            throw new CustomerEmailExistException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean getMarketingConsent(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new StoreNotFoundException()).isMarketingConsent();
    }

    @Override
    @Transactional
    public boolean updateMarketingConsent(UUID customerId, boolean enabled) {
        Customer customer = customerRepository.findByIdForUpdate(customerId).orElseThrow(() -> new StoreNotFoundException());
        customer.changeMarketingConsent(enabled);
        return customer.isMarketingConsent();
    }

    @Override
    public Customer getReferenceById(UUID id) {
        return customerRepository.getReferenceById(id);
    }

}
