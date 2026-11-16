package com.reydiazz.bodeko.core.customer.web;

import com.reydiazz.bodeko.core.customer.service.CustomerService;
import com.reydiazz.bodeko.security.config.CustomerPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** Consulta o cambia el permiso de correo del cliente autenticado. */
@RestController
@RequestMapping("/api/customer/preferences/marketing")
@RequiredArgsConstructor
public class CustomerPreferenceController {
    private final CustomerService customers;
    @GetMapping
    public MarketingConsentResponse get(@AuthenticationPrincipal CustomerPrincipal principal) {
        return new MarketingConsentResponse(customers.getMarketingConsent(principal.customer().getId()));
    }
    @PatchMapping
    public MarketingConsentResponse update(@AuthenticationPrincipal CustomerPrincipal principal,
                                           @Valid @RequestBody MarketingConsentRequest request) {
        return new MarketingConsentResponse(customers.updateMarketingConsent(principal.customer().getId(), request.enabled()));
    }
}
