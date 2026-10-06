package pe.edu.galaxy.pocintegracion.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreditCardTest {

    @Test
    void approveMasksSensitiveDataAndTrimsHolder() {
        CreditCard card = CreditCard.approve("  Ada Lovelace  ", "4111111111111111", "12345678");

        assertThat(card.getId()).isNull();
        assertThat(card.getExternalId()).startsWith("CC-");
        assertThat(card.getHolderName()).isEqualTo("Ada Lovelace");
        assertThat(card.getMaskedCardNumber()).isEqualTo("************1111");
        assertThat(card.getMaskedDocumentNumber()).isEqualTo("******78");
        assertThat(card.getStatus()).isEqualTo(CreditCardStatus.APPROVED);
    }

    @Test
    void maskDigitsHandlesShortAndEmptyValues() {
        assertThat(CreditCard.maskDigits("12", 4)).isEqualTo("12");
        assertThat(CreditCard.maskDigits("", 4)).isEmpty();
        assertThat(CreditCard.maskDigits(null, 4)).isNull();
    }

    @Test
    void restoreKeepsAllFields() {
        CreditCard card = CreditCard.restore(7L, "CC-1", "Grace", "****1", "**9", CreditCardStatus.APPROVED);

        assertThat(card.getId()).isEqualTo(7L);
        assertThat(card.getExternalId()).isEqualTo("CC-1");
    }
}
