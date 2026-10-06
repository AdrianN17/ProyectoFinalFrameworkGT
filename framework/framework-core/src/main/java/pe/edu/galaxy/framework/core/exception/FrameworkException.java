package pe.edu.galaxy.framework.core.exception;

import java.util.Map;

public class FrameworkException extends RuntimeException {

    private final String code;
    private final int httpStatus;
    private final Map<String, Object> details;

    public FrameworkException(String code, int httpStatus, String message) {
        this(code, httpStatus, message, Map.of(), null);
    }

    public FrameworkException(String code, int httpStatus, String message, Throwable cause) {
        this(code, httpStatus, message, Map.of(), cause);
    }

    public FrameworkException(
            String code,
            int httpStatus,
            String message,
            Map<String, Object> details,
            Throwable cause
    ) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
        this.details = details == null ? Map.of() : Map.copyOf(details);
    }

    public String getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Map<String, Object> getDetails() {
        return details;
    }
}
