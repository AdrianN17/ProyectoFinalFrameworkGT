package pe.edu.galaxy.pocintegracion.application.service;

import pe.edu.galaxy.framework.architecture.hexagonal.ApplicationService;
import pe.edu.galaxy.framework.cqrs.TypedQueryHandler;
import pe.edu.galaxy.pocintegracion.application.port.in.CreditCardResult;
import pe.edu.galaxy.pocintegracion.application.port.in.GetCreditCardByExternalIdQuery;
import pe.edu.galaxy.pocintegracion.domain.exception.CreditCardNotFoundException;
import pe.edu.galaxy.pocintegracion.domain.port.out.CreditCardRepositoryPort;

@ApplicationService
public class GetCreditCardByExternalIdHandler implements TypedQueryHandler<GetCreditCardByExternalIdQuery, CreditCardResult> {

    private final CreditCardRepositoryPort repository;

    public GetCreditCardByExternalIdHandler(CreditCardRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public CreditCardResult handle(GetCreditCardByExternalIdQuery query) {
        return repository.findByExternalId(query.externalId())
                .map(CreditCardResult::from)
                .orElseThrow(() -> new CreditCardNotFoundException(query.externalId()));
    }

    @Override
    public Class<GetCreditCardByExternalIdQuery> queryType() {
        return GetCreditCardByExternalIdQuery.class;
    }
}
