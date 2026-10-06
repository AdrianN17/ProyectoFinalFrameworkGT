package pe.edu.galaxy.pocintegracion.domain.exception;

public class FraudRejectedException extends RuntimeException {

    private final String reason;

    public FraudRejectedException(String reason) {
        super("Credit card rejected by fraud-check: " + reason);
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
