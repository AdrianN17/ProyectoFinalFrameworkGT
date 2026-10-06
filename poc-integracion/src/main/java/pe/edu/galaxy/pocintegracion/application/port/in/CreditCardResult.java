package pe.edu.galaxy.pocintegracion.application.port.in;

import pe.edu.galaxy.pocintegracion.domain.model.CreditCard;

public record CreditCardResult(
        Long id,
        String externalId,
        String holderName,
        String maskedCardNumber,
        String maskedDocumentNumber,
        String status
) {

    public static CreditCardResult from(CreditCard creditCard) {
        return new CreditCardResult(
                creditCard.getId(),
                creditCard.getExternalId(),
                creditCard.getHolderName(),
                creditCard.getMaskedCardNumber(),
                creditCard.getMaskedDocumentNumber(),
                creditCard.getStatus().name()
        );
    }
}
