package pe.edu.galaxy.finalapp.application;

import pe.edu.galaxy.framework.architecture.layered.ApplicationService;
import pe.edu.galaxy.framework.core.exception.FrameworkException;
import pe.edu.galaxy.framework.cqrs.TypedQueryHandler;
import pe.edu.galaxy.finalapp.domain.CreditCardAccount;
import pe.edu.galaxy.finalapp.domain.CreditCardRepository;

@ApplicationService
public class GetCreditCardByExternalIdHandler implements TypedQueryHandler<GetCreditCardByExternalIdQuery, CreditCardResult> {

    private final CreditCardRepository repository;

    public GetCreditCardByExternalIdHandler(CreditCardRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreditCardResult handle(GetCreditCardByExternalIdQuery query) {
        CreditCardAccount entity = repository.findByExternalId(query.externalId())
                .orElseThrow(() -> new FrameworkException("CREDIT_CARD_NOT_FOUND", 404, "Credit card not found"));

        return new CreditCardResult(
                entity.getId(),
                entity.getExternalId(),
                entity.getHolderName(),
                entity.getMaskedCardNumber(),
                entity.getMaskedDocumentNumber(),
                entity.getStatus()
        );
    }

    @Override
    public Class<GetCreditCardByExternalIdQuery> queryType() {
        return GetCreditCardByExternalIdQuery.class;
    }
}
