package pe.edu.galaxy.framework.bus.spring;

import pe.edu.galaxy.framework.core.exception.FrameworkException;
import pe.edu.galaxy.framework.cqrs.Command;
import pe.edu.galaxy.framework.cqrs.CommandBus;
import pe.edu.galaxy.framework.cqrs.TypedCommandHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpringCommandBus implements CommandBus {

    private final Map<Class<?>, TypedCommandHandler<?, ?>> handlers;

    public SpringCommandBus(List<TypedCommandHandler<?, ?>> handlers) {
        this.handlers = handlers.stream().collect(Collectors.toMap(TypedCommandHandler::commandType, Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R, C extends Command<R>> R dispatch(C command) {
        TypedCommandHandler<C, R> handler = (TypedCommandHandler<C, R>) handlers.get(command.getClass());
        if (handler == null) {
            throw new FrameworkException("COMMAND_HANDLER_NOT_FOUND", 500,
                    "No command handler found for " + command.getClass().getName());
        }
        return handler.handle(command);
    }
}
