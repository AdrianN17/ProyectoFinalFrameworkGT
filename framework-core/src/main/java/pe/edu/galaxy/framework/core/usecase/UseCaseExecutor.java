package pe.edu.galaxy.framework.core.usecase;

import java.util.Objects;

public final class UseCaseExecutor {

    private UseCaseExecutor() {
        throw new AssertionError("No instances");
    }

    public static <I, O> O execute(UseCase<I, O> useCase, I input) {
        Objects.requireNonNull(useCase, "useCase is required");
        return useCase.execute(input);
    }
}
