package net.ozwolf.raml.apidocs.model.v10;

import net.ozwolf.raml.apidocs.model.RamlParameter;
import org.raml.v2.api.model.v10.datamodel.ArrayTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.NumberTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.StringTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;

import java.util.List;

public class V10_RamlParameter implements RamlParameter {
    private final TypeDeclaration parameter;
    private final boolean multiple;

    public V10_RamlParameter(TypeDeclaration parameter) {
        this.parameter = getTypeDec(parameter);
        this.multiple = parameter instanceof ArrayTypeDeclaration;
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
        return parameter.displayName().value();
    }

    @Override
    public String getDescription() {
        return parameter.description().value();
    }

    @Override
    public String getPattern() {
        return (parameter instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) parameter).pattern() : null;
    }

    @Override
    public String getExample() {
        return parameter.example().value();
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

    private static TypeDeclaration getTypeDec(TypeDeclaration declaration) {
        if (declaration instanceof ArrayTypeDeclaration) return ((ArrayTypeDeclaration) declaration).items();

        return declaration;
    }
}
