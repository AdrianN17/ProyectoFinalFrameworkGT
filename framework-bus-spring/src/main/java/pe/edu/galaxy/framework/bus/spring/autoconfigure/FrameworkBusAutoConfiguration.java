package pe.edu.galaxy.framework.bus.spring.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import pe.edu.galaxy.framework.bus.spring.SpringCommandBus;
import pe.edu.galaxy.framework.bus.spring.SpringQueryBus;
import pe.edu.galaxy.framework.cqrs.CommandBus;
import pe.edu.galaxy.framework.cqrs.QueryBus;
import pe.edu.galaxy.framework.cqrs.TypedCommandHandler;
import pe.edu.galaxy.framework.cqrs.TypedQueryHandler;

import java.util.List;

@AutoConfiguration
public class FrameworkBusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(CommandBus.class)
    public CommandBus commandBus(List<TypedCommandHandler<?, ?>> handlers) {
        return new SpringCommandBus(handlers);
    }

    @Bean
    @ConditionalOnMissingBean(QueryBus.class)
    public QueryBus queryBus(List<TypedQueryHandler<?, ?>> handlers) {
        return new SpringQueryBus(handlers);
    }
}
