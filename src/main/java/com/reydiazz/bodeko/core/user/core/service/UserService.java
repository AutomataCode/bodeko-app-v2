package com.reydiazz.bodeko.core.user.core.service;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.security.auth.web.request.RegisterCustomerRequest;
import com.reydiazz.bodeko.security.auth.web.request.RegisterOwnerRequest;

import java.util.UUID;

public interface UserService {

    User createCustomerEntity(RegisterCustomerRequest request);

    User createOwnerEntity(RegisterOwnerRequest request);

    User getReferenceById(UUID id);

}
