package pe.edu.galaxy.framework.openapi.fraud;

import pe.andes.api.client.AndesApiClient;
import pe.andes.api.client.AndesApiClientRegistry;
import pe.edu.galaxy.framework.openapi.generated.fraud.model.FraudCheckRequest;
import pe.edu.galaxy.framework.openapi.generated.fraud.model.FraudCheckResponse;

public class AndesFraudCheckGateway implements FraudCheckGateway {

    private final AndesApiClientRegistry registry;

    public AndesFraudCheckGateway(AndesApiClientRegistry registry) {
        this.registry = registry;
    }

    @Override
    public FraudCheckDecision check(String cardNumber, String documentNumber) {
        AndesApiClient client = registry.get("fraudCheck");

        FraudCheckRequest request = new FraudCheckRequest();
        request.setCardNumber(cardNumber);
        request.setDocumentNumber(documentNumber);

        FraudCheckResponse response = client.post("/api/v1/fraud-check", request, FraudCheckResponse.class);

        boolean approved = Boolean.TRUE.equals(response.getApproved());
        int score = response.getScore() == null ? 0 : response.getScore();
        String reason = response.getReason() == null ? "NO_REASON" : response.getReason();
        return new FraudCheckDecision(approved, score, reason);
    }
}
