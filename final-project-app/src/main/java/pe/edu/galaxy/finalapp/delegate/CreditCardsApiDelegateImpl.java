package pe.edu.galaxy.finalapp.delegate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.galaxy.finalapp.application.CreateCreditCardCommand;
import pe.edu.galaxy.finalapp.application.CreditCardResult;
import pe.edu.galaxy.finalapp.application.GetCreditCardByExternalIdQuery;
import pe.edu.galaxy.finalapp.generated.server.api.CreditCardsApiDelegate;
import pe.edu.galaxy.finalapp.generated.server.model.CreditCard;
import pe.edu.galaxy.finalapp.generated.server.model.CreditCardEnvelope;
import pe.edu.galaxy.finalapp.generated.server.model.CreateCreditCardRequest;
import pe.edu.galaxy.framework.cqrs.CommandBus;
import pe.edu.galaxy.framework.cqrs.QueryBus;

@Service
public class CreditCardsApiDelegateImpl implements CreditCardsApiDelegate {

    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public CreditCardsApiDelegateImpl(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @Override
    public ResponseEntity<CreditCardEnvelope> createCreditCard(CreateCreditCardRequest request) {
        CreditCardResult result = commandBus.dispatch(
                new CreateCreditCardCommand(request.getHolderName(), request.getCardNumber(), request.getDocumentNumber())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(successEnvelope(result));
    }

    @Override
    public ResponseEntity<CreditCardEnvelope> getCreditCardByExternalId(String externalId) {
        CreditCardResult result = queryBus.ask(new GetCreditCardByExternalIdQuery(externalId));
        return ResponseEntity.ok(successEnvelope(result));
    }

    private CreditCardEnvelope successEnvelope(CreditCardResult result) {
        CreditCard model = new CreditCard();
        model.setId(result.id());
        model.setExternalId(result.externalId());
        model.setHolderName(result.holderName());
        model.setMaskedCardNumber(result.maskedCardNumber());
        model.setMaskedDocumentNumber(result.maskedDocumentNumber());
        model.setStatus(CreditCard.StatusEnum.fromValue(result.status()));

        CreditCardEnvelope envelope = new CreditCardEnvelope(true);
        envelope.setData(model);
        return envelope;
    }
}
