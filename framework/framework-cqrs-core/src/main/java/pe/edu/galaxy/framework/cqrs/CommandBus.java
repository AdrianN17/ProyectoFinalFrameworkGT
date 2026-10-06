package pe.edu.galaxy.framework.cqrs;

public interface CommandBus {

    <R, C extends Command<R>> R dispatch(C command);
}
