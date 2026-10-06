package pe.edu.galaxy.pocintegracion.adapter.in.web;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.galaxy.framework.core.exception.FrameworkException;
import pe.edu.galaxy.pocintegracion.domain.exception.CreditCardNotFoundException;
import pe.edu.galaxy.pocintegracion.domain.exception.FraudRejectedException;

import java.time.Instant;
import java.util.Map;

/** Traduce excepciones de dominio y del framework a respuestas HTTP. */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DomainExceptionAdvice {

    @ExceptionHandler(CreditCardNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(CreditCardNotFoundException ex) {
        return body(404, "CREDIT_CARD_NOT_FOUND", ex.getMessage(), Map.of());
    }

    @ExceptionHandler(FraudRejectedException.class)
    public ResponseEntity<Map<String, Object>> handleFraudRejected(FraudRejectedException ex) {
        return body(422, "FRAUD_REJECTED", ex.getMessage(), Map.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableBody(HttpMessageNotReadableException ex) {
        return body(400, "MALFORMED_REQUEST", "Request body is missing or malformed", Map.of());
    }

    @ExceptionHandler(FrameworkException.class)
    public ResponseEntity<Map<String, Object>> handleFrameworkException(FrameworkException ex) {
        return body(ex.getHttpStatus(), ex.getCode(), ex.getMessage(), ex.getDetails());
    }

    private ResponseEntity<Map<String, Object>> body(int status, String code, String message, Map<String, Object> details) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now().toString(),
                "code", code,
                "message", message,
                "details", details
        ));
    }
}
