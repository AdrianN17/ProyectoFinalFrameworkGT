package pe.edu.galaxy.finalapp.application;

import org.springframework.transaction.annotation.Transactional;
import pe.edu.galaxy.framework.architecture.layered.ApplicationService;
import pe.edu.galaxy.framework.core.exception.FrameworkException;
import pe.edu.galaxy.framework.cqrs.TypedCommandHandler;
import pe.edu.galaxy.framework.openapi.fraud.FraudCheckDecision;
import pe.edu.galaxy.framework.openapi.fraud.FraudCheckGateway;
import pe.edu.galaxy.finalapp.domain.CreditCardAccount;
import pe.edu.galaxy.finalapp.domain.CreditCardRepository;

import java.util.UUID;

@ApplicationService
public class CreateCreditCardHandler implements TypedCommandHandler<CreateCreditCardCommand, CreditCardResult> {

    private final CreditCardRepository repository;
    private final FraudCheckGateway fraudCheckGateway;

    public CreateCreditCardHandler(CreditCardRepository repository, FraudCheckGateway fraudCheckGateway) {
        this.repository = repository;
        this.fraudCheckGateway = fraudCheckGateway;
    }

    @Override
    @Transactional
    public CreditCardResult handle(CreateCreditCardCommand command) {
        FraudCheckDecision fraud = fraudCheckGateway.check(command.cardNumber(), command.documentNumber());
        if (!fraud.approved()) {
            throw new FrameworkException(
                    "FRAUD_REJECTED",
                    422,
                    "Credit card rejected by fraud-check: " + fraud.reason()
            );
        }

        CreditCardAccount entity = new CreditCardAccount(
                "CC-" + UUID.randomUUID(),
                command.holderName().trim(),
            maskDigits(command.cardNumber(), 4),
            maskDigits(command.documentNumber(), 2),
                "APPROVED"
        );

        CreditCardAccount saved = repository.save(entity);
        return new CreditCardResult(
                saved.getId(),
                saved.getExternalId(),
                saved.getHolderName(),
                saved.getMaskedCardNumber(),
                saved.getMaskedDocumentNumber(),
                saved.getStatus()
        );
    }

    @Override
    public Class<CreateCreditCardCommand> commandType() {
        return CreateCreditCardCommand.class;
    }

    private String maskDigits(String value, int visibleDigits) {
        if (value == null || value.isBlank()) {
            return value;
        }
        int preserve = Math.max(0, Math.min(visibleDigits, value.length()));
        int maskLength = Math.max(0, value.length() - preserve);
        return "*".repeat(maskLength) + value.substring(value.length() - preserve);
    }
}
