package net.ozwolf.raml.generator.conditions;

import org.apache.commons.lang3.StringUtils;
import org.raml.v2.api.model.v10.datamodel.StringTypeDeclaration;

public class StringParameterCondition extends AbstractParameterCondition<StringTypeDeclaration> {
    private String expectedPattern;

    public StringParameterCondition(String name) {
        super(name, StringTypeDeclaration.class);
    }

    public StringParameterCondition(String name, String description) {
        super(name, description, StringTypeDeclaration.class);
    }

    public StringParameterCondition withPattern(String pattern){
        this.expectedPattern = pattern;
        return this;
    }

    @Override
    protected boolean test(StringTypeDeclaration value) {
        if (expectedPattern != null && !StringUtils.equals(expectedPattern, value.pattern()))
            return false;

        return true;
    }
}
