package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlParameter;
import org.raml.v2.api.model.v10.datamodel.*;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.List;
import java.util.Optional;

public class V10_RamlParameter implements RamlParameter {
    private final TypeDeclaration parameter;
    private final boolean multiple;

    public V10_RamlParameter(TypeDeclaration parameter) {
        this.parameter = parameter;
        this.multiple = parameter instanceof ArrayTypeDeclaration;
    }

    @Override
    public String getName() {
        return parameter.name();
    }

    @Override
    public String getType() {
        return getTypeDec(parameter).type();
    }

    @Override
    public String getDisplayName() {
        return Optional.ofNullable(parameter.displayName()).map(AnnotableStringType::value).orElse(null);
    }

    @Override
    public String getDescription() {
        return Optional.ofNullable(parameter.description()).map(AnnotableStringType::value).orElse(null);
    }

    @Override
    public String getPattern() {
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) actual).pattern() : null;
    }

    @Override
    public String getExample() {
        return Optional.ofNullable(parameter.example()).map(ExampleSpec::value).orElse("");
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
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) actual).enumValues() : null;
    }

    @Override
    public Double getMinValue() {
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) actual).minimum() : null;
    }

    @Override
    public Double getMaxValue() {
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) actual).maximum() : null;
    }

    private static TypeDeclaration getTypeDec(TypeDeclaration declaration) {
        if (declaration instanceof ArrayTypeDeclaration) return ((ArrayTypeDeclaration) declaration).items();

        return declaration;
    }
}
