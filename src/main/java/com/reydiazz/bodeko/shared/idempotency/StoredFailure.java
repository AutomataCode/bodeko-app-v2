package com.reydiazz.bodeko.shared.idempotency;
import com.reydiazz.bodeko.shared.exception.*;
import org.springframework.http.HttpStatus;
/** Reproduce un error de negocio ya asociado a una clave, sin ejecutar otra escritura. */
public class StoredFailure extends BusinessException {
    public StoredFailure(int status, String code, String message) { super(message, new StoredCode(HttpStatus.valueOf(status),code,message)); }
    private record StoredCode(HttpStatus getHttpStatus,String getCode,String getMessage) implements ErrorCode {}
}
