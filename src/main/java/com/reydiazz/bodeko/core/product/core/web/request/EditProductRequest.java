package com.reydiazz.bodeko.core.product.core.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record EditProductRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 200, message = "Name must have at most 200 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String name,

        @Size(max = 10000, message = "La descripción admite hasta 10000 caracteres")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String description,

        @NotNull(message = "Base price is required")
        @DecimalMin(value = "0.01", message = "El precio debe ser al menos 0.01")
        @jakarta.validation.constraints.DecimalMax(value = "100000.00", message = "El precio máximo es 100000.00")
        @Digits(integer = 10, fraction = 2, message = "Base price must have at most 10 integer digits and 2 decimals")
        BigDecimal basePrice,

        @Size(max = 500, message = "Image URL must have at most 500 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String imageUrl,

        UUID categoryId,

        boolean taxExempt

) {
    /** Solo se guardan URLs públicas de imagen, nunca esquemas ejecutables o credenciales. */
    @jakarta.validation.constraints.AssertTrue(message="La imagen debe usar una URL http o https sin credenciales")
    public boolean isImageUrlValid() {
        if (imageUrl==null || imageUrl.isBlank()) return true;
        try {
            java.net.URI uri=java.net.URI.create(imageUrl.trim());
            return uri.getHost()!=null && uri.getUserInfo()==null && ("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()));
        } catch (IllegalArgumentException error) { return false; }
    }
}
