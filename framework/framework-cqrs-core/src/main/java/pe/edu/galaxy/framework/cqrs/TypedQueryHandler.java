package pe.edu.galaxy.framework.cqrs;

public interface TypedQueryHandler<Q extends Query<R>, R> extends QueryHandler<Q, R> {

    Class<Q> queryType();
}
