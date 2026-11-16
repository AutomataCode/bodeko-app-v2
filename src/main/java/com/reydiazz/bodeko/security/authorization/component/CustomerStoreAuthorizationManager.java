package com.reydiazz.bodeko.security.authorization.component;

import com.reydiazz.bodeko.security.authorization.util.StoreScopeRoutes;
import com.reydiazz.bodeko.security.config.CustomerPrincipal;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Supplier;

/** Un cliente solo puede usar el carrito, las órdenes y los pagos de la tienda donde se registró. */
@Component
public class CustomerStoreAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationResult authorize(Supplier<? extends Authentication> authentication, RequestAuthorizationContext context) {
        Authentication auth = authentication.get();
        if (auth == null || !(auth.getPrincipal() instanceof CustomerPrincipal principal)) {
            return new AuthorizationDecision(false);
        }
        String rawStoreId = context.getVariables().get(StoreScopeRoutes.STORE_ID_VARIABLE);
        try {
            UUID storeId = rawStoreId == null ? null : UUID.fromString(rawStoreId);
            return new AuthorizationDecision(principal.customer().getStoreId().equals(storeId));
        } catch (IllegalArgumentException exception) {
            return new AuthorizationDecision(false);
        }
    }

}
