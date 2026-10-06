package pe.edu.galaxy.pocintegracion.adapter.in.web.error;

import pe.andes.api.common.model.ApiError;

import java.time.Instant;
import java.util.List;

final class ApiErrors {

    private ApiErrors() {
    }

    static ApiError of(int httpStatus, String code, String message, String traceId) {
        return ApiError.builder()
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .traceId(traceId)
                .timestamp(Instant.now())
                .details(List.of())
                .build();
    }
}
