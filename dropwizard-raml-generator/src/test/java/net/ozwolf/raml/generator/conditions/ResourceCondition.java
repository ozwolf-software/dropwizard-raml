package net.ozwolf.raml.generator.conditions;

import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.resources.Resource;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

public class ResourceCondition extends Condition<Resource> {
    private final String path;
    private final String description;

    private final List<AbstractParameterCondition> uriParameters;
    private final List<MethodCondition> methods;

    public ResourceCondition(String path, String description) {
        super("<path=" + path + ", description=" + description + ">");
        this.path = path;
        this.description = description;

        this.uriParameters = newArrayList();
        this.methods = newArrayList();
    }

    public ResourceCondition(String path){
        this(path, null);
    }

    public ResourceCondition withUriParameter(AbstractParameterCondition parameter){
        this.uriParameters.add(parameter);
        return this;
    }

    public ResourceCondition withMethod(MethodCondition method){
        this.methods.add(method);
        return this;
    }

    @Override
    public boolean matches(Resource value) {
        Resource actual = value.resourcePath().equals(path) ? value : find(value);
        if (actual == null)
            return false;

        if (description != null && Optional.ofNullable(actual.description()).map(v -> !v.value().equals(description)).orElse(true))
            return false;

        if (!AbstractParameterCondition.match(uriParameters, actual.uriParameters()))
            return false;

        if (!MethodCondition.matches(methods, actual.methods()))
            return false;

        return true;
    }

    private Resource find(Resource parent){
        for(Resource child : parent.resources()){
            if (child.resourcePath().equals(path))
                return child;

            return find(child);
        }

        return null;
    }
}
