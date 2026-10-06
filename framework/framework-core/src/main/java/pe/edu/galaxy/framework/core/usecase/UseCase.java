package pe.edu.galaxy.framework.core.usecase;

@FunctionalInterface
public interface UseCase<I, O> {

    O execute(I input);
}
