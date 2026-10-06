package pe.edu.galaxy.pocintegracion.domain.model;

import java.util.UUID;

/**
 * Modelo de dominio de una tarjeta de credito. Es un POJO sin dependencias de frameworks: no
 * conoce JPA, Spring ni OpenAPI.
 */
public final class CreditCard {

    private final Long id;
    private final String externalId;
    private final String holderName;
    private final String maskedCardNumber;
    private final String maskedDocumentNumber;
    private final CreditCardStatus status;

    private CreditCard(
            Long id,
            String externalId,
            String holderName,
            String maskedCardNumber,
            String maskedDocumentNumber,
            CreditCardStatus status
    ) {
        this.id = id;
        this.externalId = externalId;
        this.holderName = holderName;
        this.maskedCardNumber = maskedCardNumber;
        this.maskedDocumentNumber = maskedDocumentNumber;
        this.status = status;
    }

    /** Crea una tarjeta nueva aprobada; enmascara los datos sensibles y aun no tiene id persistido. */
    public static CreditCard approve(String holderName, String cardNumber, String documentNumber) {
        return new CreditCard(
                null,
                "CC-" + UUID.randomUUID(),
                holderName.trim(),
                maskDigits(cardNumber, 4),
                maskDigits(documentNumber, 2),
                CreditCardStatus.APPROVED
        );
    }

    /** Reconstruye una tarjeta ya persistida. */
    public static CreditCard restore(
            Long id,
            String externalId,
            String holderName,
            String maskedCardNumber,
            String maskedDocumentNumber,
            CreditCardStatus status
    ) {
        return new CreditCard(id, externalId, holderName, maskedCardNumber, maskedDocumentNumber, status);
    }

    static String maskDigits(String value, int visibleDigits) {
        if (value == null || value.isBlank()) {
            return value;
        }
        int preserve = Math.max(0, Math.min(visibleDigits, value.length()));
        int maskLength = Math.max(0, value.length() - preserve);
        return "*".repeat(maskLength) + value.substring(value.length() - preserve);
    }

    public Long getId() {
        return id;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public String getMaskedDocumentNumber() {
        return maskedDocumentNumber;
    }

    public CreditCardStatus getStatus() {
        return status;
    }
}
