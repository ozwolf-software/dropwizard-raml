package net.ozwolf.raml.generator.media;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public interface MediaFactory {
    Optional<String> create(Class<?> type, ObjectMapper mapper);
}
