package pe.edu.galaxy.framework.openapi.fraud;

public record FraudCheckDecision(boolean approved, int score, String reason) {
}
