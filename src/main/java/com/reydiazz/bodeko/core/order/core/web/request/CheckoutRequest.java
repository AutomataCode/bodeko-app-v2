package com.reydiazz.bodeko.core.order.core.web.request;

import com.reydiazz.bodeko.core.order.core.model.enums.CustomerDocumentType;
import com.reydiazz.bodeko.core.order.core.model.enums.InvoiceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CheckoutRequest(

        @NotNull(message = "Invoice type is required")
        InvoiceType invoiceType,

        @NotNull(message = "Document type is required")
        CustomerDocumentType documentType,

        @NotBlank(message = "Document number is required")
        @Size(max = 20, message = "Document number must have at most 20 characters")
        String documentNumber,

        @Size(max = 200, message = "Business name must have at most 200 characters")
        String businessName,

        @Size(max = 300, message = "Billing address must have at most 300 characters")
        String billingAddress

) {
}
