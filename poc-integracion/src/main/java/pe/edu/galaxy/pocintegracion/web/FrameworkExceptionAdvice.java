package pe.edu.galaxy.pocintegracion.web;

import java.time.Instant;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pe.edu.galaxy.framework.core.exception.FrameworkException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FrameworkExceptionAdvice {

    @ExceptionHandler(FrameworkException.class)
    public ResponseEntity<Map<String, Object>> handleFrameworkException(FrameworkException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(Map.of(
                "timestamp", Instant.now().toString(),
                "code", ex.getCode(),
                "message", ex.getMessage(),
                "details", ex.getDetails()
        ));
    }
}