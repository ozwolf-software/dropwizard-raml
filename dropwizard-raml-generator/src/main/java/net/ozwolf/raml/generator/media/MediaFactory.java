package net.ozwolf.raml.generator.media;

import java.util.Optional;

public interface MediaFactory {
    Optional<String> create(Class<?> type, boolean collection);
}
