package com.reydiazz.bodeko.security.auth.web.request;

import jakarta.validation.constraints.*;

public record RegisterOwnerRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 255, message = "Email must have at most 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must have at least 8 characters")
        String password,

        @NotBlank(message = "Full name is required")
        @Size(max = 150, message = "Full name must have at most 150 characters")
        String fullName,

        @NotNull
        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Phone number must include the country code")
        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        String phoneNumber

) {
}
