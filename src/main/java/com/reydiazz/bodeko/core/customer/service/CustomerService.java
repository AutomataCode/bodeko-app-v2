package com.reydiazz.bodeko.core.customer.service;

import com.reydiazz.bodeko.core.customer.model.entity.Customer;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;

import java.util.UUID;

public interface CustomerService {

    Customer create(UUID storeId, RegisterCustomerRequest request);

    Customer getReferenceById(UUID id);

    boolean getMarketingConsent(UUID customerId);

    boolean updateMarketingConsent(UUID customerId, boolean enabled);

}
