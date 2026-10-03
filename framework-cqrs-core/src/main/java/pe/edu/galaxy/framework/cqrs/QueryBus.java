package pe.edu.galaxy.framework.cqrs;

public interface QueryBus {

    <R, Q extends Query<R>> R ask(Q query);
}
