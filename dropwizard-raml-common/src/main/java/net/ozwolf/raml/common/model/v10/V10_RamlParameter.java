package net.ozwolf.raml.common.model.v10;

import net.ozwolf.raml.common.model.RamlParameter;
import org.raml.v2.api.model.v10.datamodel.*;
import org.raml.v2.api.model.v10.system.types.AnnotableStringType;

import java.util.List;
import java.util.Optional;

/**
 * <h1>RAML 1.0 Parameter</h1>
 *
 * A 1.0 definition of a parameter
 */
public class V10_RamlParameter implements RamlParameter {
    private final TypeDeclaration parameter;
    private final boolean multiple;

    /**
     * Create a new 1.0 parameter item
     *
     * @param parameter the parameter
     */
    public V10_RamlParameter(TypeDeclaration parameter) {
        this.parameter = parameter;
        this.multiple = parameter instanceof ArrayTypeDeclaration;
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
        return getTypeDec(parameter).type();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDisplayName() {
        return Optional.ofNullable(parameter.displayName()).map(AnnotableStringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDescription() {
        return Optional.ofNullable(parameter.description()).map(AnnotableStringType::value).orElse(null);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getPattern() {
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) actual).pattern() : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getExample() {
        return Optional.ofNullable(parameter.example()).map(ExampleSpec::value).orElse("");
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getDefault() {
        return parameter.defaultValue();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isRequired() {
        return parameter.required();
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
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof StringTypeDeclaration) ? ((StringTypeDeclaration) actual).enumValues() : null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Double getMinValue() {
        TypeDeclaration actual = getTypeDec(parameter);
        return (actual instanceof NumberTypeDeclaration) ? ((NumberTypeDeclaration) actual).minimum() : null;
    }

    /**
     * @inheritDoc
     */
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
