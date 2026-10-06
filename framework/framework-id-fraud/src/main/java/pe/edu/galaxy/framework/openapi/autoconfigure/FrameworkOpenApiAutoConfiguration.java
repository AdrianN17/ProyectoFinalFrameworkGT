package pe.edu.galaxy.framework.openapi.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import pe.andes.api.client.AndesApiClientRegistry;
import pe.edu.galaxy.framework.openapi.fraud.AndesFraudCheckGateway;
import pe.edu.galaxy.framework.openapi.fraud.FraudCheckGateway;

@AutoConfiguration
public class FrameworkOpenApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FraudCheckGateway.class)
    public FraudCheckGateway fraudCheckGateway(AndesApiClientRegistry registry) {
        return new AndesFraudCheckGateway(registry);
    }
}
