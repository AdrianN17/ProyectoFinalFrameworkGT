package pe.edu.galaxy.pocintegracion.application.port.in;

import pe.edu.galaxy.framework.architecture.hexagonal.InboundPort;
import pe.edu.galaxy.framework.cqrs.Query;

@InboundPort
public record GetCreditCardByExternalIdQuery(String externalId) implements Query<CreditCardResult> {
}
