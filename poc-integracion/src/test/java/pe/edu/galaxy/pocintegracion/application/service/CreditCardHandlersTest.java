package pe.edu.galaxy.pocintegracion.application.service;

import org.junit.jupiter.api.Test;
import pe.edu.galaxy.pocintegracion.application.port.in.CreateCreditCardCommand;
import pe.edu.galaxy.pocintegracion.application.port.in.CreditCardResult;
import pe.edu.galaxy.pocintegracion.application.port.in.GetCreditCardByExternalIdQuery;
import pe.edu.galaxy.pocintegracion.domain.exception.CreditCardNotFoundException;
import pe.edu.galaxy.pocintegracion.domain.exception.FraudRejectedException;
import pe.edu.galaxy.pocintegracion.domain.model.CreditCard;
import pe.edu.galaxy.pocintegracion.domain.port.out.CreditCardRepositoryPort;
import pe.edu.galaxy.pocintegracion.domain.port.out.FraudCheckPort;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Prueba los casos de uso solo con puertos falsos: sin Spring, base de datos ni HTTP. */
class CreditCardHandlersTest {

    private final InMemoryRepository repository = new InMemoryRepository();

    @Test
    void createApprovedCardPersistsIt() {
        FraudCheckPort approve = (card, doc) -> new FraudCheckPort.FraudAssessment(true, 0, "ok");
        CreateCreditCardHandler handler = new CreateCreditCardHandler(repository, approve);

        CreditCardResult result = handler.handle(new CreateCreditCardCommand("Ada", "4111111111111111", "12345678"));

        assertThat(result.id()).isNotNull();
        assertThat(result.externalId()).startsWith("CC-");
        assertThat(result.maskedCardNumber()).isEqualTo("************1111");
        assertThat(result.status()).isEqualTo("APPROVED");
        assertThat(repository.store).hasSize(1);
    }

    @Test
    void createRejectedByFraudDoesNotPersist() {
        FraudCheckPort reject = (card, doc) -> new FraudCheckPort.FraudAssessment(false, 99, "high risk");
        CreateCreditCardHandler handler = new CreateCreditCardHandler(repository, reject);

        assertThatThrownBy(() -> handler.handle(new CreateCreditCardCommand("Grace", "4111111111110000", "87654321")))
                .isInstanceOf(FraudRejectedException.class)
                .hasMessageContaining("high risk");
        assertThat(repository.store).isEmpty();
    }

    @Test
    void getReturnsExistingCard() {
        CreditCard saved = repository.save(CreditCard.approve("Ada", "4111111111111111", "12345678"));
        GetCreditCardByExternalIdHandler handler = new GetCreditCardByExternalIdHandler(repository);

        CreditCardResult result = handler.handle(new GetCreditCardByExternalIdQuery(saved.getExternalId()));

        assertThat(result.externalId()).isEqualTo(saved.getExternalId());
    }

    @Test
    void getUnknownThrowsNotFound() {
        GetCreditCardByExternalIdHandler handler = new GetCreditCardByExternalIdHandler(repository);

        assertThatThrownBy(() -> handler.handle(new GetCreditCardByExternalIdQuery("CC-missing")))
                .isInstanceOf(CreditCardNotFoundException.class);
    }

    private static final class InMemoryRepository implements CreditCardRepositoryPort {

        private final Map<String, CreditCard> store = new HashMap<>();

        @Override
        public CreditCard save(CreditCard creditCard) {
            CreditCard persisted = CreditCard.restore(
                    (long) store.size() + 1,
                    creditCard.getExternalId(),
                    creditCard.getHolderName(),
                    creditCard.getMaskedCardNumber(),
                    creditCard.getMaskedDocumentNumber(),
                    creditCard.getStatus()
            );
            store.put(persisted.getExternalId(), persisted);
            return persisted;
        }

        @Override
        public Optional<CreditCard> findByExternalId(String externalId) {
            return Optional.ofNullable(store.get(externalId));
        }
    }
}
