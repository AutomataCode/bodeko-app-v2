package com.reydiazz.bodeko.security.auth.web.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email cannot be empty")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String email,
        @NotBlank(message = "Password cannot be empty")
        @jakarta.validation.constraints.Pattern(regexp="^[^\\x00]*$",message="El campo contiene un carácter nulo no permitido")
        String password
) {    /** BCrypt admite hasta 72 bytes UTF-8; una entrada mayor debe dar 400, no 500. */
    @jakarta.validation.constraints.AssertTrue(message="La contraseña admite como máximo 72 bytes UTF-8")
    public boolean isPasswordLengthValid() {
        return password == null || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72;
    }
}
