package net.ozwolf.raml.common.model.v08;

import net.ozwolf.raml.common.model.RamlParameter;
import org.raml.v2.api.model.v08.parameters.NumberTypeDeclaration;
import org.raml.v2.api.model.v08.parameters.Parameter;
import org.raml.v2.api.model.v08.parameters.StringTypeDeclaration;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;

/**
 * <h1>RAML 0.8 Parameter</h1>
 *
 * A 0.8 definition of a parameter
 */
public class V08_RamlParameter implements RamlParameter {
    private final Parameter parameter;
    private final boolean multiple;

    /**
     * Create a new 0.8 parameter item
     *
     * @param parameter the parameter
     */
    public V08_RamlParameter(Parameter parameter) {
        this.parameter = parameter;
        this.multiple = parameter.repeat() != null && parameter.repeat();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getName() {
        return parameter.name();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getType() {
        return parameter.type();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDisplayName() {
        return parameter.displayName();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(parameter.description()).map(StringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPattern() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).pattern() : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getExample() {
        return Optional.ofNullable(parameter.example()).orElse("");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDefault() {
        return Optional.ofNullable(parameter.defaultValue()).orElse("");
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isRequired() {
        return Optional.ofNullable(parameter.required()).orElse(false);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * @inheritDoc
     */
    @Override
    public List<String> getAllowedValues() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).enumValues() : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Double getMinValue() {
        return (parameter instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) parameter).minimum() : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Double getMaxValue() {
        return (parameter instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) parameter).maximum() : null;
    }
}
