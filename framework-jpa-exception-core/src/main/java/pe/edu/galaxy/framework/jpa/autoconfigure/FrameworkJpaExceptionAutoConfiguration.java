package pe.edu.galaxy.framework.jpa.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import pe.edu.galaxy.framework.exception.web.FrameworkGlobalExceptionHandler;

@AutoConfiguration
public class FrameworkJpaExceptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FrameworkGlobalExceptionHandler frameworkGlobalExceptionHandler() {
        return new FrameworkGlobalExceptionHandler();
    }
}
