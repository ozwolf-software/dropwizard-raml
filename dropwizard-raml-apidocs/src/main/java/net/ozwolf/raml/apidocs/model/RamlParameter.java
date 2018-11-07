package net.ozwolf.raml.apidocs.model;

import net.ozwolf.raml.apidocs.util.MarkDownHelper;

import java.util.List;

public interface RamlParameter {
    String getName();

    String getType();

    String getDisplayName();

    String getDescription();

    default String getDescriptionHtml() {
        return MarkDownHelper.toHtml(getDescription());
    }

    String getPattern();

    String getExample();

    String getDefault();

    boolean isRequired();

    boolean isMultiple();

    List<String> getAllowedValues();

    Double getMinValue();

    Double getMaxValue();
}
