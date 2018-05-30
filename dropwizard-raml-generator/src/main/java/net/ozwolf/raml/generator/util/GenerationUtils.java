package net.ozwolf.raml.generator.util;

import net.ozwolf.raml.generator.exception.RamlGenerationException;
import net.ozwolf.raml.generator.exception.RamlGenerationUnhandledException;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class GenerationUtils {
    public static <T> T generate(Callable<T> provider,
                                 RamlGenerationException errors){
        try {
            return provider.call();
        } catch (RamlGenerationException e){
            errors = errors.record(e);
            return null;
        } catch (Exception e){
            throw new RamlGenerationUnhandledException(e);
        }
    }
}
