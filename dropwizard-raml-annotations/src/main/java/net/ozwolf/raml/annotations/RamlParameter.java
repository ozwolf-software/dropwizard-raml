package net.ozwolf.raml.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <h1>RAML Parameter Annotation</h1>
 *
 * Annotation to describe a parameter.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RamlParameter {
    /**
     * The name of the parameter.
     *
     * @return the parameter name
     */
    String name();

    /**
     * The type of parameter.
     *
     * If describing an array parameter, define the item type here.
     *
     * Must align with the [Allowable Built-In Types](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md#built-in-types)
     *
     * @return the parameter type
     */
    String type();

    /**
     * The display name of the parameter.
     *
     * @return the parameter display name
     */
    String displayName() default "";

    /**
     * The description of the parameter
     *
     * @return the parameter description
     */
    String description() default "";

    /**
     * Flag if the parameter is required or not.
     *
     * @return the required flag
     */
    boolean required() default true;

    /**
     * Flag if the parameter accepts multiple values.
     *
     * @return the multiple flag
     */
    boolean multiple() default false;

    /**
     * The list of allowed values as a string list.
     *
     * If the `allowedValuesEnum()` property is defined, this is ignored.
     *
     * @return the list of allowed values
     */
    String[] allowedValues() default {};

    /**
     * The enum the allowed values for the parameter are derived from.
     *
     * @return the allowed values enum class
     */
    Class<? extends Enum> allowedValuesEnum() default NoEnum.class;

    /**
     * The example of the parameter.
     *
     * @return the parameter example
     */
    String example() default "";

    /**
     * The regular expression pattern the parameter must adhere to.
     *
     * Only suitable for string-based parameters.
     *
     * @return the regex pattern
     */
    String pattern() default "";

    /**
     * The default for the parameter if not supplied.
     *
     * @return the parameter default
     */
    String defaultValue() default "";

    /**
     * The minimum value the parameter is allowed to have.
     *
     * Only suitable for numeric-based parameters.
     *
     * @return the parameter minimum value
     */
    long minimum() default Long.MIN_VALUE;

    /**
     * The maximum value the parameter is allowed to have.
     *
     * Only suitable for numeric-based parameters.
     *
     * @return the parameter maximum value
     */
    long maximum() default Long.MIN_VALUE;

    enum NoEnum {}
}
