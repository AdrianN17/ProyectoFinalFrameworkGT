package pe.edu.galaxy.pocintegracion.application;

public record CreditCardResult(
        Long id,
        String externalId,
        String holderName,
        String maskedCardNumber,
        String maskedDocumentNumber,
        String status
) {
}
