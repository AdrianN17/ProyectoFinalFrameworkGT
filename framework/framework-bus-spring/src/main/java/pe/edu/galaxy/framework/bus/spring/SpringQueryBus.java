package pe.edu.galaxy.framework.bus.spring;

import pe.edu.galaxy.framework.core.exception.FrameworkException;
import pe.edu.galaxy.framework.cqrs.Query;
import pe.edu.galaxy.framework.cqrs.QueryBus;
import pe.edu.galaxy.framework.cqrs.TypedQueryHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpringQueryBus implements QueryBus {

    private final Map<Class<?>, TypedQueryHandler<?, ?>> handlers;

    public SpringQueryBus(List<TypedQueryHandler<?, ?>> handlers) {
        this.handlers = handlers.stream().collect(Collectors.toMap(TypedQueryHandler::queryType, Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, Q extends Query<R>> R ask(Q query) {
        TypedQueryHandler<Q, R> handler = (TypedQueryHandler<Q, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new FrameworkException("QUERY_HANDLER_NOT_FOUND", 500,
                    "No query handler found for " + query.getClass().getName());
        }
        return handler.handle(query);
    }
}
