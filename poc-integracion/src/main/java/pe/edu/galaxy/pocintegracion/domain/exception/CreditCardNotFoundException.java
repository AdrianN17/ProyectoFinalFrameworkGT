package pe.edu.galaxy.pocintegracion.domain.exception;

public class CreditCardNotFoundException extends RuntimeException {

    private final String externalId;

    public CreditCardNotFoundException(String externalId) {
        super("Credit card not found");
        this.externalId = externalId;
    }

    public String getExternalId() {
        return externalId;
    }
}
