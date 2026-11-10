package com.reydiazz.bodeko.security.authorization.component;

import com.reydiazz.bodeko.core.store.repository.StoreRepository;
import com.reydiazz.bodeko.core.user.core.model.enums.UserRole;
import com.reydiazz.bodeko.security.authorization.util.StoreScopeRoutes;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class StoreOwnerAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private static final String ADMIN_AUTHORITY = "ROLE_" + UserRole.ADMIN.name();

    private final StoreRepository storeRepository;

    @Override
    public AuthorizationResult authorize(Supplier<? extends Authentication> authentication, RequestAuthorizationContext context) {
        return new AuthorizationDecision(isStoreOwner(authentication.get(), context));
    }

    private boolean isStoreOwner(Authentication authentication, RequestAuthorizationContext context) {
        if (!(authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal)) {
            return false;
        }
        if (!hasAdminRole(authentication)) {
            return false;
        }
        UUID storeId = extractStoreId(context);
        return storeId != null && storeRepository.existsByIdAndOwnerId(storeId, principal.user().getId());
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> ADMIN_AUTHORITY.equals(authority.getAuthority()));
    }

    private UUID extractStoreId(RequestAuthorizationContext context) {
        String rawStoreId = context.getVariables().get(StoreScopeRoutes.STORE_ID_VARIABLE);
        try {
            return rawStoreId == null ? null : UUID.fromString(rawStoreId);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

}
