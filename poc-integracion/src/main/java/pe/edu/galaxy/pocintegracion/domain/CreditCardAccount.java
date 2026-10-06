package pe.edu.galaxy.pocintegracion.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.galaxy.framework.jpa.BaseJpaEntity;

@Entity
@Table(name = "credit_cards")
public class CreditCardAccount extends BaseJpaEntity {

    @Column(name = "external_id", nullable = false, unique = true, length = 50)
    private String externalId;

    @Column(name = "holder_name", nullable = false, length = 150)
    private String holderName;

    @Column(name = "masked_card_number", nullable = false, length = 30)
    private String maskedCardNumber;

    @Column(name = "masked_document_number", nullable = false, length = 30)
    private String maskedDocumentNumber;

    @Column(name = "status", nullable = false, length = 20)
    private String status;

    protected CreditCardAccount() {
    }

    public CreditCardAccount(
            String externalId,
            String holderName,
            String maskedCardNumber,
            String maskedDocumentNumber,
            String status
    ) {
        this.externalId = externalId;
        this.holderName = holderName;
        this.maskedCardNumber = maskedCardNumber;
        this.maskedDocumentNumber = maskedDocumentNumber;
        this.status = status;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getMaskedCardNumber() {
        return maskedCardNumber;
    }

    public String getMaskedDocumentNumber() {
        return maskedDocumentNumber;
    }

    public String getStatus() {
        return status;
    }
}
