package net.ozwolf.raml.validator.validator;

import net.ozwolf.raml.common.model.RamlParameter;
import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static net.ozwolf.raml.validator.testutil.SpecificationViolationConditions.violationOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterValidatorTest {
    @Test
    public void shouldPassValidation() {
        RamlParameter parameter = stringParameter(false, "value1", "value2");
        List<String> values = newArrayList("value1");

        Node node = new Node("parameters");

        List<SpecificationViolation> result = new ParameterValidator().validate(node, parameter, values);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldFailValidationWhenRequiredParameterHasNoValues() {
        RamlParameter parameter = stringParameter(false);
        List<String> values = newArrayList();

        Node node = new Node("stringParam");

        List<SpecificationViolation> result = new ParameterValidator().validate(node, parameter, values);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("stringParam.required", "must be provided"));
    }

    @Test
    public void shouldFailValidationWhenNonMultipleParametersHasMultipleValues() {
        RamlParameter parameter = stringParameter(false);
        List<String> values = newArrayList("value1", "value2");

        Node node = new Node("stringParam");

        List<SpecificationViolation> result = new ParameterValidator().validate(node, parameter, values);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("stringParam.multiple", "must be a single value"));
    }

    @Test
    public void shouldFailStringValidation() {
        RamlParameter parameter = stringParameter(true, "value1");
        List<String> values = newArrayList("value1", "wrong");

        Node node = new Node("stringParam");

        List<SpecificationViolation> result = new ParameterValidator().validate(node, parameter, values);

        assertThat(result)
                .hasSize(2)
                .areAtLeastOne(violationOf("stringParam.[1].allowed", "must be one of [ value1 ]"))
                .areAtLeastOne(violationOf("stringParam.[1].pattern", "must match pattern [ value[0-9] ]"));
    }

    @Test
    public void shouldFailNumericValidationWithAnyValues() {
        RamlParameter parameter = numberParameter(1.0, 10.0);
        List<String> values = newArrayList("2", "0", "11", "wrong");

        Node node = new Node("numberParam");

        List<SpecificationViolation> result = new ParameterValidator().validate(node, parameter, values);

        assertThat(result)
                .hasSize(3)
                .areAtLeastOne(violationOf("numberParam.[1].min", "must be >= [ 1.0 ]"))
                .areAtLeastOne(violationOf("numberParam.[2].max", "must be <= [ 10.0 ]"))
                .areAtLeastOne(violationOf("numberParam.[3].type", "must be numeric"));
    }

    @Test
    public void shouldFailNumbericValidationWithAlloweddValues() {
        RamlParameter parameter = numberParameter(1.0, 10.0, "2");
        List<String> values = newArrayList("2", "3");

        Node node = new Node("numberParam");

        List<SpecificationViolation> result = new ParameterValidator().validate(node, parameter, values);

        assertThat(result)
                .hasSize(1)
                .areAtLeastOne(violationOf("numberParam.[1].allowed", "must be one of [ 2 ]"));
    }

    private static RamlParameter stringParameter(boolean multiple, String... allowedValues) {
        RamlParameter parameter = mock(RamlParameter.class);
        when(parameter.getName()).thenReturn("stringParam");
        when(parameter.getType()).thenReturn("String");
        when(parameter.isNumericType()).thenReturn(false);
        when(parameter.getAllowedValues()).thenReturn(newArrayList(allowedValues));
        when(parameter.getPattern()).thenReturn("value[0-9]");
        when(parameter.isMultiple()).thenReturn(multiple);
        when(parameter.isRequired()).thenReturn(true);
        return parameter;
    }

    private static RamlParameter numberParameter(double min, double max, String... allowedValues) {
        RamlParameter parameter = mock(RamlParameter.class);
        when(parameter.getName()).thenReturn("numberParam");
        when(parameter.isNumericType()).thenReturn(true);
        when(parameter.getAllowedValues()).thenReturn(newArrayList(allowedValues));
        when(parameter.isMultiple()).thenReturn(true);
        when(parameter.isRequired()).thenReturn(true);
        when(parameter.getMinValue()).thenReturn(min);
        when(parameter.getMaxValue()).thenReturn(max);
        return parameter;
    }
}