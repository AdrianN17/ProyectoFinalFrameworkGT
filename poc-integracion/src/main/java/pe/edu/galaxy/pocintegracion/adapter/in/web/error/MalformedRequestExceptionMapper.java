package pe.edu.galaxy.pocintegracion.adapter.in.web.error;

import org.springframework.http.converter.HttpMessageNotReadableException;
import pe.andes.api.common.model.ApiError;
import pe.andes.api.server.error.AndesExceptionMapper;
import pe.edu.galaxy.framework.architecture.hexagonal.InboundAdapter;

@InboundAdapter
public class MalformedRequestExceptionMapper implements AndesExceptionMapper<HttpMessageNotReadableException> {

    @Override
    public Class<HttpMessageNotReadableException> getExceptionType() {
        return HttpMessageNotReadableException.class;
    }

    @Override
    public int getHttpStatus() {
        return 400;
    }

    @Override
    public ApiError map(HttpMessageNotReadableException ex, String traceId) {
        return ApiErrors.of(400, "MALFORMED_REQUEST", "Request body is missing or malformed", traceId);
    }
}
