package net.ozwolf.raml.generator.factory;

public class FactoryResult<T> {
    private final T result;
    private final RuntimeException error;

    public FactoryResult(T result) {
        this.result = result;
        this.error = null;
    }

    public FactoryResult(RuntimeException error) {
        this.error = error;
        this.result = null;
    }

    public boolean isInError() {
        return error != null;
    }

    public T getResult() {
        if (error != null) throw error;
        return result;
    }

    public RuntimeException getError() {
        return error;
    }
}
