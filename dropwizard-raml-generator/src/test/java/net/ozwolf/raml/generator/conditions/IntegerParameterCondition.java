package net.ozwolf.raml.generator.conditions;

import org.raml.v2.api.model.v10.datamodel.IntegerTypeDeclaration;

import java.util.Optional;

public class IntegerParameterCondition extends AbstractParameterCondition<IntegerTypeDeclaration> {
    private Integer min;
    private Integer max;

    public IntegerParameterCondition(String name, String description){
        super(name, description, IntegerTypeDeclaration.class);
    }

    public IntegerParameterCondition(String name){
        this(name, null);
    }

    public IntegerParameterCondition withMin(Integer min){
        this.min = min;
        return this;
    }

    public IntegerParameterCondition withMax(Integer max){
        this.max = max;
        return this;
    }

    @Override
    protected boolean test(IntegerTypeDeclaration value) {
        if (min != null && Optional.ofNullable(value.minimum()).map(v -> min.doubleValue() != v).orElse(true))
            return false;

        if (max != null && Optional.ofNullable(value.maximum()).map(v -> max.doubleValue() != v).orElse(true))
            return false;

        return true;
    }
}
