package pe.edu.galaxy.pocintegracion.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pe.edu.galaxy.pocintegracion.domain.port.out.FraudCheckPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/** Prueba de extremo a extremo: HTTP real -> adaptador REST -> casos de uso -> adaptador JPA (H2). */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreditCardApiIntegrationTest {

    private static final Pattern EXTERNAL_ID = Pattern.compile("\"externalId\"\\s*:\\s*\"(CC-[^\"]+)\"");

    @Value("${local.server.port}")
    private int port;

    @MockitoBean
    private FraudCheckPort fraudCheckPort;

    private final HttpClient client = HttpClient.newHttpClient();

    private static final String VALID_BODY =
            "{\"holderName\":\"Ada Lovelace\",\"cardNumber\":\"4111111111111111\",\"documentNumber\":\"12345678\"}";

    @Test
    void createThenGetReturns201And200() throws Exception {
        when(fraudCheckPort.check(anyString(), anyString()))
                .thenReturn(new FraudCheckPort.FraudAssessment(true, 0, "ok"));

        HttpResponse<String> created = post(VALID_BODY);

        assertThat(created.statusCode()).isEqualTo(201);
        assertThat(created.body()).contains("\"maskedCardNumber\":\"************1111\"").contains("APPROVED");

        Matcher matcher = EXTERNAL_ID.matcher(created.body());
        assertThat(matcher.find()).isTrue();

        HttpResponse<String> fetched = get("/api/v1/credit-cards/" + matcher.group(1));
        assertThat(fetched.statusCode()).isEqualTo(200);
        assertThat(fetched.body()).contains(matcher.group(1));
    }

    @Test
    void unknownExternalIdReturns404() throws Exception {
        HttpResponse<String> response = get("/api/v1/credit-cards/CC-does-not-exist");

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(response.body()).contains("\"success\":false").contains("CREDIT_CARD_NOT_FOUND");
    }

    @Test
    void fraudRejectionReturns422() throws Exception {
        when(fraudCheckPort.check(anyString(), anyString()))
                .thenReturn(new FraudCheckPort.FraudAssessment(false, 99, "blocked"));

        HttpResponse<String> response = post(VALID_BODY);

        assertThat(response.statusCode()).isEqualTo(422);
        assertThat(response.body()).contains("\"success\":false").contains("FRAUD_REJECTED");
    }

    @Test
    void invalidBodyReturnsValidationError() throws Exception {
        HttpResponse<String> response = post("{\"holderName\":\"\"}");

        // La validacion de request la resuelve el starter Andes (422 VALIDATION_ERROR).
        assertThat(response.statusCode()).as(response.body()).isEqualTo(422);
        assertThat(response.body()).contains("VALIDATION_ERROR");
    }

    @Test
    void malformedJsonReturns400() throws Exception {
        HttpResponse<String> response = post("{ \"holderName\": \"Juan Perez\", ");

        assertThat(response.statusCode()).as(response.body()).isEqualTo(400);
        assertThat(response.body()).contains("\"success\":false").contains("MALFORMED_REQUEST");
    }

    private HttpResponse<String> post(String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/v1/credit-cards"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
