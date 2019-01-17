package net.ozwolf.raml.common.model;

import net.ozwolf.raml.common.util.MarkDownHelper;

import java.util.List;

/**
 * <h1>RAML Parameter</h1>
 *
 * A parameter specification (URI, query or header)
 */
public interface RamlParameter {
    /**
     * The parameter name
     *
     * @return the name
     */
    String getName();

    /**
     * The parameter type
     *
     * @return the type
     */
    String getType();

    /**
     * The parameter display name
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * The description of what the parameter represents in Markdown
     *
     * @return the description markdown
     */
    String getDescription();

    /**
     * The description of what the parameter represents in HTML
     *
     * @return the description HTML
     */
    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    /**
     * The parameter regex pattern it adheres to
     *
     * @return the regular expression
     */
    String getPattern();

    /**
     * An example of the parameter
     *
     * @return the example
     */
    String getExample();

    /**
     * The default value of the parameter
     *
     * @return the default value
     */
    String getDefault();

    /**
     * A flag indicating if the parameter is compulsory
     *
     * @return the required flag
     */
    boolean isRequired();

    /**
     * A flag indicating if the parameter can be used to send multiple values
     *
     * @return the multiple flag
     */
    boolean isMultiple();

    /**
     * The list of allowed values
     *
     * @return the allowed values
     */
    List<String> getAllowedValues();

    /**
     * The minimum value allowed
     *
     * @return the minimum
     */
    Double getMinValue();

    /**
     * The maximum value allowed
     *
     * @return the maximum
     */
    Double getMaxValue();

    default boolean isNumericType() {
        return this.getType().equalsIgnoreCase("Number") || this.getType().equalsIgnoreCase("Integer");
    }
}
