package com.reydiazz.bodeko.core.payment.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePaymentRequest(

        @NotBlank(message = "Payment token is required")
        @Size(max = 100, message = "Payment token must have at most 100 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String sourceId

) {
}
