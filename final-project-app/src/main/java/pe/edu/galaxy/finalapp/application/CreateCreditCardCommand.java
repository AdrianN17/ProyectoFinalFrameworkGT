package pe.edu.galaxy.finalapp.application;

import pe.edu.galaxy.framework.cqrs.Command;

public record CreateCreditCardCommand(
        String holderName,
        String cardNumber,
        String documentNumber
) implements Command<CreditCardResult> {
}
