package pe.edu.galaxy.framework.exception.web;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.edu.galaxy.framework.core.exception.FrameworkException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class FrameworkGlobalExceptionHandler {

    @ExceptionHandler(FrameworkException.class)
    public ResponseEntity<Map<String, Object>> handleFrameworkException(FrameworkException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(Map.of(
                "timestamp", Instant.now().toString(),
                "code", ex.getCode(),
                "message", ex.getMessage(),
                "details", ex.getDetails()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "timestamp", Instant.now().toString(),
                "code", "DATA_INTEGRITY_VIOLATION",
                "message", "Data integrity constraint violated"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", Instant.now().toString(),
                "code", "INTERNAL_ERROR",
                "message", "Unexpected error"
        ));
    }
}
