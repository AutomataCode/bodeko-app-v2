package com.reydiazz.bodeko.security.auth.web.request;

import jakarta.validation.constraints.*;

public record RegisterCustomerRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 255, message = "Email must have at most 255 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must have at least 8 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String password,

        @NotBlank(message = "Full name is required")
        @Size(max = 150, message = "Full name must have at most 150 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String fullName,

        @Pattern(
                regexp = "^\\+[1-9]\\d{7,14}$",
                message = "Phone number must include the country code")
        @Size(max = 20, message = "Phone number cannot exceed 20 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String phoneNumber,

        com.reydiazz.bodeko.core.user.core.model.enums.UserDocumentType documentType,

        @Size(max = 20, message = "Document number must have at most 20 characters")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String documentNumber

) {
    /** Si se agrega un documento, valida su tipo y número. */
    @AssertTrue(message = "Document type and number must match")
    public boolean isDocumentValid() {
        if (documentType == null) return documentNumber == null || documentNumber.isBlank();
        if (documentNumber == null) return false;
        return switch (documentType) {
            case DNI -> documentNumber.matches("\\d{8}");
            case RUC -> documentNumber.matches("\\d{11}");
            case CE, PASSPORT -> documentNumber.matches("[a-zA-Z0-9]{6,20}");
        };
    }
    /** BCrypt admite hasta 72 bytes UTF-8; una entrada mayor debe dar 400, no 500. */
    @jakarta.validation.constraints.AssertTrue(message="La contraseña admite como máximo 72 bytes UTF-8")
    public boolean isPasswordLengthValid() {
        return password == null || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72;
    }
}
