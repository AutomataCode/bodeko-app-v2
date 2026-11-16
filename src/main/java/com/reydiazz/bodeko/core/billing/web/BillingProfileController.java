package com.reydiazz.bodeko.core.billing.web;

import com.reydiazz.bodeko.core.billing.service.BillingIssuerService;
import com.reydiazz.bodeko.core.billing.service.SalesDocumentService;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Configuración del perfil fiscal compartido entre tiendas del propietario. */
@RestController
@RequestMapping("/api/stores/{storeId}/billing-profile")
@RequiredArgsConstructor
public class BillingProfileController {
    private final BillingIssuerService issuerService;
    private final SalesDocumentService documentService;

    @GetMapping
    public BillingProfileResponse get(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal) {
        return issuerService.get(storeId, principal.user().getId());
    }

    @PutMapping
    public BillingProfileResponse update(@PathVariable UUID storeId, @AuthenticationPrincipal UserPrincipal principal,
                                         @Valid @RequestBody BillingProfileRequest request) {
        BillingProfileResponse response = issuerService.update(storeId, principal.user().getId(), request);
        documentService.issuePendingForIssuer(response.issuerId());
        documentService.issuePendingForStore(storeId);
        return response;
    }
}
