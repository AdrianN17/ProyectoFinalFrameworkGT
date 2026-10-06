package pe.edu.galaxy.framework.cqrs;

public interface QueryHandler<Q extends Query<R>, R> {

    R handle(Q query);
}
