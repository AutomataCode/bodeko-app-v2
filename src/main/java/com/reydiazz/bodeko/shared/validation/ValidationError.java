package com.reydiazz.bodeko.shared.validation;

import com.reydiazz.bodeko.shared.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/** Errores de entrada y de concurrencia esperados por los clientes de la API. */
@Getter
@RequiredArgsConstructor
public enum ValidationError implements ErrorCode {
    PRODUCT_DUPLICATE("PRODUCT_DUPLICATE", "Ya existe un producto con ese nombre en esta tienda.", HttpStatus.CONFLICT),
    STORE_DUPLICATE("STORE_DUPLICATE", "Ya tienes una tienda con ese nombre.", HttpStatus.CONFLICT),
    INVALID_DISCOUNT("INVALID_DISCOUNT", "El descuento debe estar entre 0.01 y 99.99 por ciento.", HttpStatus.BAD_REQUEST),
    IDEMPOTENCY_REQUIRED("IDEMPOTENCY_REQUIRED", "Envía Idempotency-Key con entre 8 y 128 caracteres alfanuméricos, punto, guion o dos puntos.", HttpStatus.BAD_REQUEST),
    IDEMPOTENCY_CONFLICT("IDEMPOTENCY_CONFLICT", "La clave de idempotencia ya se utilizó con otros datos.", HttpStatus.CONFLICT),
    OPERATION_IN_PROGRESS("OPERATION_IN_PROGRESS", "La operación está en proceso. Reintenta con la misma clave.", HttpStatus.CONFLICT),
    OPERATION_UNCERTAIN("OPERATION_UNCERTAIN", "El resultado del proveedor requiere revisión. No vuelvas a cobrar o devolver hasta conciliarlo.", HttpStatus.CONFLICT),
    QUANTITY_LIMIT("QUANTITY_LIMIT", "La cantidad máxima por producto es 10000.", HttpStatus.BAD_REQUEST),
    PRICE_LIMIT("PRICE_LIMIT", "El precio debe ser mayor que cero y permitir cobros en céntimos dentro del límite del proveedor.", HttpStatus.BAD_REQUEST);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
