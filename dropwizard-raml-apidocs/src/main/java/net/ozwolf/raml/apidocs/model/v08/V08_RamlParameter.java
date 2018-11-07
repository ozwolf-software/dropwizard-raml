package net.ozwolf.raml.apidocs.model.v08;

import net.ozwolf.raml.apidocs.model.RamlParameter;
import org.raml.v2.api.model.v08.parameters.NumberTypeDeclaration;
import org.raml.v2.api.model.v08.parameters.Parameter;
import org.raml.v2.api.model.v08.parameters.StringTypeDeclaration;
import org.raml.v2.api.model.v08.system.types.StringType;

import java.util.List;
import java.util.Optional;

public class V08_RamlParameter implements RamlParameter {
    private final Parameter parameter;
    private final boolean multiple;

    public V08_RamlParameter(Parameter parameter) {
        this.parameter = parameter;
        this.multiple = parameter.repeat();
    }

    @Override
    public String getName() {
        return parameter.name();
    }

    @Override
    public String getType() {
        return parameter.type();
    }

    @Override
    public String getDisplayName() {
        return parameter.displayName();
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(parameter.description()).map(StringType::value).orElse(null);
    }

    @Override
    public String getPattern() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).pattern() : null;
    }

    @Override
    public String getExample() {
        return parameter.example();
    }

    @Override
    public String getDefault() {
        return parameter.defaultValue();
    }

    @Override
    public boolean isRequired() {
        return parameter.required();
    }

    @Override
    public boolean isMultiple() {
        return multiple;
    }

    @Override
    public List<String> getAllowedValues() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).enumValues() : null;
    }

    @Override
    public Double getMinValue() {
        return (parameter instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) parameter).minimum() : null;
    }

    @Override
    public Double getMaxValue() {
        return (parameter instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) parameter).maximum() : null;
    }
}
