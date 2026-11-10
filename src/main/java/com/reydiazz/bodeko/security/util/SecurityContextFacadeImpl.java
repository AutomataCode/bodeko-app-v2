package com.reydiazz.bodeko.security.util;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityContextFacadeImpl implements SecurityContextFacade {

    @Override
    public UserPrincipal getPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return extractPrincipal(auth);
    }

    @Override
    public UserPrincipal extractPrincipal(Authentication auth) {
        return Optional.ofNullable(auth)
                .map(Authentication::getPrincipal)
                .filter(UserPrincipal.class::isInstance)
                .map(UserPrincipal.class::cast)
                .orElse(null);
    }

    @Override
    public User getCurrentUserEntity() {
        return getPrincipal().user();
    }

}