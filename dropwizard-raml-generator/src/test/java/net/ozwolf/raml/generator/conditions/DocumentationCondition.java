package net.ozwolf.raml.generator.conditions;

import org.assertj.core.api.Condition;
import org.raml.v2.api.model.v10.api.DocumentationItem;

import java.util.Optional;

public class DocumentationCondition extends Condition<DocumentationItem> {
    private final String title;
    private final String content;

    public DocumentationCondition(String title, String content) {
        super("<title=" + title + ">");
        this.title = title;
        this.content = content;
    }

    public DocumentationCondition(String title) {
        this(title, null);
    }

    @Override
    public boolean matches(DocumentationItem value) {
        if (!title.equals(value.title().value()))
            return false;

        return content == null || !Optional.ofNullable(value.content()).map(v -> !v.value().equals(content)).orElse(true);
    }
}
