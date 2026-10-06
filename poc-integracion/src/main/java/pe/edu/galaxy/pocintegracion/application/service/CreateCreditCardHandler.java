package pe.edu.galaxy.pocintegracion.application.service;

import org.springframework.transaction.annotation.Transactional;
import pe.edu.galaxy.framework.architecture.hexagonal.ApplicationService;
import pe.edu.galaxy.framework.cqrs.TypedCommandHandler;
import pe.edu.galaxy.pocintegracion.application.port.in.CreateCreditCardCommand;
import pe.edu.galaxy.pocintegracion.application.port.in.CreditCardResult;
import pe.edu.galaxy.pocintegracion.domain.exception.FraudRejectedException;
import pe.edu.galaxy.pocintegracion.domain.model.CreditCard;
import pe.edu.galaxy.pocintegracion.domain.port.out.CreditCardRepositoryPort;
import pe.edu.galaxy.pocintegracion.domain.port.out.FraudCheckPort;

@ApplicationService
public class CreateCreditCardHandler implements TypedCommandHandler<CreateCreditCardCommand, CreditCardResult> {

    private final CreditCardRepositoryPort repository;
    private final FraudCheckPort fraudCheck;

    public CreateCreditCardHandler(CreditCardRepositoryPort repository, FraudCheckPort fraudCheck) {
        this.repository = repository;
        this.fraudCheck = fraudCheck;
    }

    @Override
    @Transactional
    public CreditCardResult handle(CreateCreditCardCommand command) {
        FraudCheckPort.FraudAssessment assessment = fraudCheck.check(command.cardNumber(), command.documentNumber());
        if (!assessment.approved()) {
            throw new FraudRejectedException(assessment.reason());
        }

        CreditCard approved = CreditCard.approve(command.holderName(), command.cardNumber(), command.documentNumber());
        return CreditCardResult.from(repository.save(approved));
    }

    @Override
    public Class<CreateCreditCardCommand> commandType() {
        return CreateCreditCardCommand.class;
    }
}
