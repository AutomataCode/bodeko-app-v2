package com.reydiazz.bodeko.security.util;

import com.reydiazz.bodeko.core.user.core.model.entity.User;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import org.springframework.security.core.Authentication;

public interface SecurityContextFacade {

    UserPrincipal getPrincipal();

    UserPrincipal extractPrincipal(Authentication authentication);

    User getCurrentUserEntity();

}