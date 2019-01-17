package net.ozwolf.raml.validator.util;

import javax.ws.rs.core.MediaType;
import java.util.Optional;

public class MediaTypeUtils {
    public static Optional<MediaType> getTypeFor(String contentType){
        try {
            return Optional.ofNullable(MediaType.valueOf(contentType));
        } catch (IllegalArgumentException e){
            return Optional.empty();
        }
    }
}
