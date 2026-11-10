package com.reydiazz.bodeko.core.store.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateStoreRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 150, message = "Name must have at most 150 characters")
        String name,

        @NotBlank(message = "Tax ID is required")
        @Pattern(regexp = "^\\d{11}$", message = "Tax ID must have exactly 11 digits")
        String taxId,

        @NotBlank(message = "Legal name is required")
        @Size(max = 200, message = "Legal name must have at most 200 characters")
        String legalName,

        @NotBlank(message = "Subdomain is required")
        @Size(max = 63, message = "Subdomain must have at most 63 characters")
        @Pattern(
                regexp = "^[a-z0-9]([a-z0-9-]*[a-z0-9])?$",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Subdomain can only contain letters, numbers and hyphens, and cannot start or end with a hyphen")
        String subdomain

) {
}
