package pe.edu.galaxy.framework.openapi.fraud;

public interface FraudCheckGateway {

    FraudCheckDecision check(String cardNumber, String documentNumber);
}
