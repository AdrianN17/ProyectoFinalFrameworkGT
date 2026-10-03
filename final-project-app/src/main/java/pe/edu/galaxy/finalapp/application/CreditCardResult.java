package pe.edu.galaxy.finalapp.application;

public record CreditCardResult(
        Long id,
        String externalId,
        String holderName,
        String maskedCardNumber,
        String maskedDocumentNumber,
        String status
) {
}
