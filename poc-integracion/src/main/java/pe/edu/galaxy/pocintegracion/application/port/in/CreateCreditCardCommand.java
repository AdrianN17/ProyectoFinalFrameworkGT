package pe.edu.galaxy.pocintegracion.application.port.in;

import pe.edu.galaxy.framework.architecture.hexagonal.InboundPort;
import pe.edu.galaxy.framework.cqrs.Command;

@InboundPort
public record CreateCreditCardCommand(
        String holderName,
        String cardNumber,
        String documentNumber
) implements Command<CreditCardResult> {
}
