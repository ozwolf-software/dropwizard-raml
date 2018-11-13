package net.ozwolf.raml.generator.conditions;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;
import org.raml.v2.api.model.v10.system.types.MarkdownString;

import java.util.List;
import java.util.Optional;

public abstract class AbstractParameterCondition<T extends TypeDeclaration> extends Condition<TypeDeclaration> {
    private final String name;
    private final String description;

    private final Class<T> expectedType;

    AbstractParameterCondition(String name, Class<T> expectedType) {
        this(name, null, expectedType);
    }

    AbstractParameterCondition(String name, String description, Class<T> expectedType) {
        super("<name=" + name + ", description=" + description + ">");
        this.name = name;
        this.description = description;
        this.expectedType = expectedType;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean matches(TypeDeclaration value) {
        if (!expectedType.isInstance(value))
            return false;

        if (!value.name().equals(name))
            return false;

        if (description != null) {
            String actual = Optional.ofNullable(value.description()).map(MarkdownString::value).orElse(null);
            if (!StringUtils.equals(description, actual))
                return false;
        }

        return test(expectedType.cast(value));
    }

    protected abstract boolean test(T value);

    public static boolean match(List<AbstractParameterCondition> expectedParameters, List<TypeDeclaration> actualParameters){
        for(AbstractParameterCondition parameter : expectedParameters){
            TypeDeclaration actual = actualParameters.stream().filter(p -> p.name().equals(parameter.getName())).findFirst().orElse(null);
            if (actual == null || !parameter.matches(actual))
                return false;
        }
        return true;
    }
}
