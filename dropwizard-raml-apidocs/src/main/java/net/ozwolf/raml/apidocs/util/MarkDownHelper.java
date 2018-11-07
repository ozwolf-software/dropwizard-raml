package net.ozwolf.raml.apidocs.util;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.apache.commons.lang3.StringUtils;

public class MarkDownHelper {
    private final static Parser PARSER = Parser.builder().build();
    private final static HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();

    public static String toHtml(String markdown) {
        if (StringUtils.isBlank(markdown))
            return "";

        Node node = PARSER.parse(markdown);

        String result = HTML_RENDERER.render(node);
        if (StringUtils.startsWithIgnoreCase(result, "<p>") && StringUtils.endsWithIgnoreCase(result, "</p>\n"))
            result = StringUtils.stripEnd(StringUtils.stripStart(result, "<p>"), "</p>\n");

        return result;
    }
}
