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
    PRODUCT_VARIANT_DUPLICATE("PRODUCT_VARIANT_DUPLICATE", "El SKU o la combinación de opciones ya existe en esta tienda.", HttpStatus.CONFLICT),
    STORE_DUPLICATE("STORE_DUPLICATE", "Ya tienes una tienda con ese nombre.", HttpStatus.CONFLICT),
    INVALID_DISCOUNT("INVALID_DISCOUNT", "El descuento debe estar entre 0.01 y 99.99 por ciento.", HttpStatus.BAD_REQUEST),
    IDEMPOTENCY_REQUIRED("IDEMPOTENCY_REQUIRED", "Envía Idempotency-Key con entre 8 y 128 caracteres alfanuméricos, punto, guion o dos puntos.", HttpStatus.BAD_REQUEST),
    IDEMPOTENCY_CONFLICT("IDEMPOTENCY_CONFLICT", "La clave de idempotencia ya se utilizó con otros datos.", HttpStatus.CONFLICT),
    OPERATION_IN_PROGRESS("OPERATION_IN_PROGRESS", "La operación está en proceso. Reintenta con la misma clave.", HttpStatus.CONFLICT),
    OPERATION_UNCERTAIN("OPERATION_UNCERTAIN", "El resultado del proveedor requiere revisión. No vuelvas a cobrar o devolver hasta conciliarlo.", HttpStatus.CONFLICT),
    QUANTITY_LIMIT("QUANTITY_LIMIT", "La cantidad máxima por producto es 10000.", HttpStatus.BAD_REQUEST),
    INVENTORY_MOVEMENT_DIRECTION("INVENTORY_MOVEMENT_DIRECTION", "El tipo de movimiento no coincide con el cambio de stock.", HttpStatus.BAD_REQUEST),
    BILLING_PROFILE_CONFLICT("BILLING_PROFILE_CONFLICT", "Ese RUC ya está asociado a otra razón social de tu cuenta.", HttpStatus.CONFLICT),
    BILLING_PROFILE_NOT_FOUND("BILLING_PROFILE_NOT_FOUND", "No se encontró el perfil fiscal de esta tienda.", HttpStatus.NOT_FOUND),
    SALES_DOCUMENT_NOT_FOUND("SALES_DOCUMENT_NOT_FOUND", "No se encontró el comprobante de esta orden.", HttpStatus.NOT_FOUND),
    SALES_DOCUMENT_ORDER_NOT_PAID("SALES_DOCUMENT_ORDER_NOT_PAID", "El comprobante estará disponible cuando el pago esté confirmado.", HttpStatus.CONFLICT),
    PRICE_LIMIT("PRICE_LIMIT", "El precio debe ser mayor que cero y permitir cobros en céntimos dentro del límite del proveedor.", HttpStatus.BAD_REQUEST),
    INVALID_SHRINKAGE_TYPE("INVALID_SHRINKAGE_TYPE", "Una merma debe registrarse como daño o pérdida.", HttpStatus.BAD_REQUEST),
    SHRINKAGE_INSUFFICIENT_STOCK("SHRINKAGE_INSUFFICIENT_STOCK", "La cantidad de merma supera el stock disponible de la variante.", HttpStatus.CONFLICT),
    SUPPLIER_DUPLICATE("SUPPLIER_DUPLICATE", "Ya existe un proveedor con ese nombre en esta tienda.", HttpStatus.CONFLICT),
    SUPPLIER_NOT_FOUND("SUPPLIER_NOT_FOUND", "No se encontró el proveedor activo de esta tienda.", HttpStatus.NOT_FOUND),
    PURCHASE_ORDER_NOT_FOUND("PURCHASE_ORDER_NOT_FOUND", "No se encontró la orden de compra de esta tienda.", HttpStatus.NOT_FOUND),
    PURCHASE_ORDER_ITEM_NOT_FOUND("PURCHASE_ORDER_ITEM_NOT_FOUND", "No se encontró el artículo de la orden de compra.", HttpStatus.NOT_FOUND),
    PURCHASE_ORDER_VARIANT_NOT_FOUND("PURCHASE_ORDER_VARIANT_NOT_FOUND", "La variante no pertenece a esta tienda.", HttpStatus.NOT_FOUND),
    PURCHASE_ORDER_DUPLICATE_VARIANT("PURCHASE_ORDER_DUPLICATE_VARIANT", "Agrega cada variante una sola vez por orden de compra.", HttpStatus.BAD_REQUEST),
    PURCHASE_ORDER_NOT_RECEIVABLE("PURCHASE_ORDER_NOT_RECEIVABLE", "La orden de compra ya no admite recepciones.", HttpStatus.CONFLICT),
    PURCHASE_ORDER_INVALID_RECEIPT("PURCHASE_ORDER_INVALID_RECEIPT", "Las cantidades recibidas y rechazadas deben ser válidas y no superar lo pendiente.", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("ORDER_NOT_FOUND", "No se encontró la orden de esta tienda.", HttpStatus.NOT_FOUND),
    ORDER_TRACKING_REQUIRES_SHIPPED("ORDER_TRACKING_REQUIRES_SHIPPED", "Agrega el seguimiento cuando la orden esté enviada.", HttpStatus.CONFLICT);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
