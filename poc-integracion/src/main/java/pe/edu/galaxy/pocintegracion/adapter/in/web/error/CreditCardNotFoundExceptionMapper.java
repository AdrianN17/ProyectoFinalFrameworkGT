package pe.edu.galaxy.pocintegracion.adapter.in.web.error;

import pe.andes.api.common.model.ApiError;
import pe.andes.api.server.error.AndesExceptionMapper;
import pe.edu.galaxy.framework.architecture.hexagonal.InboundAdapter;
import pe.edu.galaxy.pocintegracion.domain.exception.CreditCardNotFoundException;

@InboundAdapter
public class CreditCardNotFoundExceptionMapper implements AndesExceptionMapper<CreditCardNotFoundException> {

    @Override
    public Class<CreditCardNotFoundException> getExceptionType() {
        return CreditCardNotFoundException.class;
    }

    @Override
    public int getHttpStatus() {
        return 404;
    }

    @Override
    public ApiError map(CreditCardNotFoundException ex, String traceId) {
        return ApiErrors.of(404, "CREDIT_CARD_NOT_FOUND", ex.getMessage(), traceId);
    }
}
