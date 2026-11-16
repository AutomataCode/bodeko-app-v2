package com.reydiazz.bodeko.core.billing.web;

import com.reydiazz.bodeko.core.billing.service.SalesDocumentService;
import com.reydiazz.bodeko.security.config.CustomerPrincipal;
import com.reydiazz.bodeko.security.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/** Consulta segura de los documentos por parte del cliente o del propietario. */
@RestController
@RequiredArgsConstructor
public class SalesDocumentController {
    private final SalesDocumentService documentService;

    @GetMapping("/api/customer/orders/{orderId}/sales-document")
    public SalesDocumentResponse getForCustomer(@PathVariable UUID orderId,
                                                @AuthenticationPrincipal CustomerPrincipal principal) {
        return documentService.getForCustomer(principal.customer().getId(), orderId);
    }

    @GetMapping("/api/stores/{storeId}/orders/{orderId}/sales-document")
    public SalesDocumentResponse getForStore(@PathVariable UUID storeId, @PathVariable UUID orderId,
                                             @AuthenticationPrincipal UserPrincipal principal) {
        return documentService.getForStore(storeId, orderId);
    }
}
