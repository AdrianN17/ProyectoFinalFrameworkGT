package pe.edu.galaxy.framework.cqrs;

public interface TypedCommandHandler<C extends Command<R>, R> extends CommandHandler<C, R> {

    Class<C> commandType();
}
