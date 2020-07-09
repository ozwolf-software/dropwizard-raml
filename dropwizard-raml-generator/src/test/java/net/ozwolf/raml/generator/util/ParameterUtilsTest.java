package net.ozwolf.raml.generator.util;

import org.junit.Test;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterUtilsTest {
    @Test
    public void shouldCorrectlyResolveTypes() {
        assertThat(ParameterUtils.getRealType(parameter("arg0"))).isEqualTo(TestEnum.class);
        assertThat(ParameterUtils.getRealType(parameter("arg1"))).isEqualTo(TestEnum.class);
        assertThat(ParameterUtils.getRealType(parameter("arg2"))).isEqualTo(String.class);
        assertThat(ParameterUtils.getRealType(parameter("arg3"))).isEqualTo(TestEnum.class);
        assertThat(ParameterUtils.getRealType(parameter("arg4"))).isEqualTo(String.class);
    }

    private static Parameter parameter(String name) {
        return Arrays.stream(TestClass.class.getDeclaredMethods())
                .filter(m -> m.getName().equals("testMethod"))
                .findFirst()
                .flatMap(m ->
                        Arrays.stream(m.getParameters())
                                .filter(p -> p.getName().equals(name))
                                .findFirst()
                )
                .orElseThrow(() -> new IllegalStateException("Could not find parameter [ " + name + " ] on [ testMethod ] method"));
    }

    public static class TestClass {
        public void testMethod(
                List<TestEnum> parameter1,
                List<? extends TestEnum> parameter2,
                List<String> parameter3,
                TestEnum parameter4,
                String parameter5
        ) {
            // do nothing
        }
    }

    public enum TestEnum {
        VALUE1,
        VALUE2;
    }
}