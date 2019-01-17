package net.ozwolf.raml.validator.validator;

import com.google.common.annotations.VisibleForTesting;
import net.ozwolf.raml.common.model.RamlBody;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import net.ozwolf.raml.validator.media.JsonMediaValidator;
import net.ozwolf.raml.validator.media.MediaValidator;
import net.ozwolf.raml.validator.media.XmlMediaValidator;
import net.ozwolf.raml.validator.util.MediaTypeUtils;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class BodyValidator {
    private final Map<MediaType, MediaValidator> validators;

    BodyValidator() {
        this.validators = defaultValidators();
    }

    @VisibleForTesting
    BodyValidator(Map<MediaType, MediaValidator> validators) {
        this.validators = validators;
    }

    public List<SpecificationViolation> validate(Node node, RamlBody body, byte[] content) {
        List<SpecificationViolation> violations = newArrayList();

        MediaValidator validator = getValidatorFor(body.getContentType()).orElse(null);
        if (validator == null)
            return violations;

        violations.addAll(validator.validate(node.createChild(body.getContentType()), body.getSchema(), content));

        return violations;
    }

    private Optional<MediaValidator> getValidatorFor(String contentType) {
        return MediaTypeUtils.getTypeFor(contentType)
                .flatMap(t ->
                        validators.entrySet()
                                .stream()
                                .filter(e -> e.getKey().isCompatible(t))
                                .findFirst()
                                .map(Map.Entry::getValue)
                );
    }

    private static Map<MediaType, MediaValidator> defaultValidators() {
        Map<MediaType, MediaValidator> validators = newHashMap();
        validators.put(MediaType.APPLICATION_JSON_TYPE, JsonMediaValidator.newInstance());
        validators.put(MediaType.APPLICATION_XML_TYPE, XmlMediaValidator.newInstance());
        validators.put(MediaType.TEXT_XML_TYPE, XmlMediaValidator.newInstance());
        return validators;
    }
}
