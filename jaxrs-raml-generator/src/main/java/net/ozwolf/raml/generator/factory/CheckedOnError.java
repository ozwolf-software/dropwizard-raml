package net.ozwolf.raml.generator.factory;

import net.ozwolf.raml.generator.exception.RamlGenerationError;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class CheckedOnError implements Consumer<RamlGenerationError> {
    private final AtomicBoolean inError = new AtomicBoolean(false);
    private final Consumer<RamlGenerationError> delegate;

    public CheckedOnError(Consumer<RamlGenerationError> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void accept(RamlGenerationError error) {
        this.inError.set(true);
        this.delegate.accept(error);
    }

    public boolean isInError() {
        return inError.get();
    }
}
