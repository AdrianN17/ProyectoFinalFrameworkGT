package pe.edu.galaxy.pocintegracion.adapter.in.web.error;

import pe.andes.api.common.model.ApiError;
import pe.andes.api.server.error.AndesExceptionMapper;
import pe.edu.galaxy.framework.architecture.hexagonal.InboundAdapter;
import pe.edu.galaxy.pocintegracion.domain.exception.FraudRejectedException;

@InboundAdapter
public class FraudRejectedExceptionMapper implements AndesExceptionMapper<FraudRejectedException> {

    @Override
    public Class<FraudRejectedException> getExceptionType() {
        return FraudRejectedException.class;
    }

    @Override
    public int getHttpStatus() {
        return 422;
    }

    @Override
    public ApiError map(FraudRejectedException ex, String traceId) {
        return ApiErrors.of(422, "FRAUD_REJECTED", ex.getMessage(), traceId);
    }
}
