package net.ozwolf.raml.validator.validator;

import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class ParameterValidator {
    ParameterValidator() {
    }

    public List<SpecificationViolation> validate(Node node, RamlParameter parameter, List<String> values) {
        List<SpecificationViolation> violations = newArrayList();

        if (parameter.isRequired() && values.isEmpty()) {
            violations.add(new SpecificationViolation(node.createChild("required"), "must be provided"));
            return violations;
        }

        if (!parameter.isMultiple() && values.size() > 1) {
            violations.add(new SpecificationViolation(node.createChild("multiple"), "must be a single value"));
        }

        if (parameter.isNumericType()) {
            validateNumber(parameter, values, node, violations);
        } else {
            validateText(parameter, values, node, violations);
        }

        return violations;
    }

    private void validateText(RamlParameter parameter, List<String> values, Node parent, List<SpecificationViolation> violations) {
        for (int index = 0; index < values.size(); index++) {
            Node thisParent = values.size() > 1 ? new Node(parent, index) : parent;

            String value = values.get(index);

            if (parameter.getAllowedValues().size() > 0 && parameter.getAllowedValues().stream().noneMatch(v -> v.equalsIgnoreCase(value))) {
                violations.add(new SpecificationViolation(thisParent.createChild("allowed"), "must be one of [ " + StringUtils.join(parameter.getAllowedValues(), ", ") + " ]"));
            }

            if (parameter.getPattern() != null) {
                Matcher matcher = Pattern.compile(parameter.getPattern()).matcher(value);
                if (!matcher.matches())
                    violations.add(new SpecificationViolation(thisParent.createChild("pattern"), "must match pattern [ " + parameter.getPattern() + " ]"));
            }
        }
    }

    private void validateNumber(RamlParameter parameter, List<String> values, Node parent, List<SpecificationViolation> violations) {
        for (int index = 0; index < values.size(); index++) {
            Node thisParent = values.size() > 1 ? new Node(parent, index) : parent;
            String value = values.get(index);
            if (!NumberUtils.isCreatable(value)) {
                violations.add(new SpecificationViolation(thisParent.createChild("type"), "must be numeric"));
                continue;
            }

            Double v = NumberUtils.createDouble(value);

            if (parameter.getAllowedValues().size() > 0 && parameter.getAllowedValues().stream().noneMatch(av -> NumberUtils.createDouble(av).equals(v))) {
                violations.add(new SpecificationViolation(thisParent.createChild("allowed"), "must be one of [ " + StringUtils.join(parameter.getAllowedValues(), ", ") + " ]"));
            }

            if (parameter.getMinValue() != null && v.compareTo(parameter.getMinValue()) < 0) {
                violations.add(new SpecificationViolation(thisParent.createChild("min"), "must be >= [ " + parameter.getMinValue() + " ]"));
            }

            if (parameter.getMaxValue() != null && v.compareTo(parameter.getMaxValue()) > 0) {
                violations.add(new SpecificationViolation(thisParent.createChild("max"), "must be <= [ " + parameter.getMaxValue() + " ]"));
            }
        }
    }
}
