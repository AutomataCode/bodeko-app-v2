package com.reydiazz.bodeko.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handlerBusiness(BusinessException exception) {
        ErrorCode error = exception.getError();
        HttpStatus status = error.getHttpStatus();
        String message=switch (error.getCode()) {
            case "USER_EMAIL_EXIST" -> "Este correo ya está registrado.";
            case "STORE_TAX_ID_ALREADY_IN_USE" -> "Este RUC ya está registrado.";
            case "STORE_SUBDOMAIN_ALREADY_IN_USE" -> "Ese subdominio ya está en uso.";
            case "PRODUCT_NOT_FOUND" -> "El producto no existe o no está publicado en esta tienda.";
            case "INVENTORY_INSUFFICIENT_STOCK" -> "No hay suficiente stock para completar la compra.";
            case "CART_PRODUCT_NOT_AVAILABLE", "ORDER_PRODUCT_NOT_AVAILABLE" -> "El producto no está disponible para comprar.";
            case "ORDER_INVALID_STATUS_TRANSITION" -> "Ese cambio de estado no está permitido.";
            case "PRODUCT_OFFER_OVERLAP" -> "La oferta se superpone con otra promoción del producto.";
            case "PRODUCT_OFFER_INVALID_DATE_RANGE" -> "La fecha final debe ser posterior al inicio.";
            default -> java.util.Objects.toString(exception.getMessage(),error.getMessage());
        };
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code(error.getCode())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<FieldErrorResponse> fieldErrors = exception.getBindingResult().getFieldErrors().stream().map(
                error -> new FieldErrorResponse(error.getField(), switch (error.getCode()==null?"":error.getCode()) {
                    case "NotNull", "NotBlank" -> "Este campo es obligatorio.";
                    case "Email" -> "Ingresa un correo válido.";
                    case "Size" -> "La longitud del campo no está permitida.";
                    case "Min", "Max", "DecimalMin", "DecimalMax", "Digits" -> "Revisa el rango y los decimales permitidos.";
                    default -> "El campo no cumple el formato o las reglas permitidas.";
                })).toList();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("VALIDATION_ERROR")
                .message("Hay campos inválidos. Revisa los datos enviados.")
                .fieldErrors(fieldErrors)
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("INVALID_REQUEST")
                .message("El cuerpo de la solicitud contiene valores inválidos o campos obligatorios ausentes.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("METHOD_NOT_ALLOWED")
                .message("The HTTP method '%s' is not supported for this route.".formatted(exception.getMethod()))
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException exception) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("RESOURCE_NOT_FOUND")
                .message("La ruta solicitada no existe.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        String detail=exception.getMostSpecificCause().getMessage();
        String code="DATA_INTEGRITY_VIOLATION";
        String message="Los datos entran en conflicto con un registro existente.";
        if (detail!=null) {
            if (detail.contains("uq_users_email") || detail.contains("users_email_key")) { code="USER_EMAIL_EXIST"; message="Este correo ya está registrado."; }
            else if (detail.contains("uq_products_store_name")) { code="PRODUCT_DUPLICATE"; message="Ya existe un producto con ese nombre en esta tienda."; }
            else if (detail.contains("uq_stores_owner_name")) { code="STORE_DUPLICATE"; message="Ya tienes una tienda con ese nombre."; }
            else if (detail.contains("subdomain")) { code="STORE_SUBDOMAIN_ALREADY_IN_USE"; message="Ese subdominio ya está en uso."; }
            else if (detail.contains("tax_id")) { code="STORE_TAX_ID_ALREADY_IN_USE"; message="Este RUC ya está registrado."; }
            else if (detail.contains("uq_categories")) { code="CATEGORY_NAME_ALREADY_IN_USE"; message="Ya existe una categoría con ese nombre."; }
        }
        log.warn("Conflicto de integridad controlado: {}",code);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
            .status(409).code(code).message(message).build());
    }

    @ExceptionHandler({org.springframework.web.bind.MissingServletRequestParameterException.class,
            org.springframework.web.bind.ServletRequestBindingException.class,
            org.springframework.web.method.annotation.HandlerMethodValidationException.class,
            jakarta.validation.ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleMissingOrInvalid(Exception exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder().status(400).code("VALIDATION_ERROR")
            .message("Faltan parámetros obligatorios o sus valores no son válidos.").build());
    }

    @ExceptionHandler(org.springframework.web.HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleContentType(Exception exception) {
        return ResponseEntity.status(415).body(ErrorResponse.builder().status(415).code("UNSUPPORTED_MEDIA_TYPE")
            .message("Envía el cuerpo como application/json.").build());
    }

    @ExceptionHandler({org.springframework.dao.ConcurrencyFailureException.class, jakarta.persistence.LockTimeoutException.class})
    public ResponseEntity<ErrorResponse> handleConcurrency(Exception exception) {
        return ResponseEntity.status(409).body(ErrorResponse.builder().status(409).code("CONCURRENT_UPDATE")
            .message("Otra solicitud está modificando estos datos. Consulta el resultado antes de reintentar.").build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unexpected error", exception);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("INTERNAL_SERVER_ERROR")
                .message("No se pudo completar la operación. Contacta al equipo si el problema continúa.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("BAD_CREDENTIALS")
                .message("El correo o la contraseña son incorrectos.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException exception) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("ACCOUNT_DISABLED")
                .message("Access is denied because your account is disabled.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("AUTHENTICATION_REQUIRED")
                .message("Inicia sesión para continuar.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .code("INVALID_PARAMETER_TYPE")
                .message("Un parámetro tiene un tipo o valor inválido.")
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .code("ACCESS_DENIED")
                .message("No tienes permiso para realizar esta operación.")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
