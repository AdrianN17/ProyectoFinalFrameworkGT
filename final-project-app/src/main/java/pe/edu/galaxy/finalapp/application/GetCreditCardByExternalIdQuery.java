package pe.edu.galaxy.finalapp.application;

import pe.edu.galaxy.framework.cqrs.Query;

public record GetCreditCardByExternalIdQuery(String externalId) implements Query<CreditCardResult> {
}
