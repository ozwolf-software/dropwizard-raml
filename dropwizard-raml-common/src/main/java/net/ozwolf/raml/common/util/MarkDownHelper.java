package net.ozwolf.raml.common.util;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.apache.commons.lang3.StringUtils;

/**
 * <h1>MarkDown Helper</h1>
 *
 * A helper utility class for handling markdown content
 */
public class MarkDownHelper {
    private final static Parser PARSER = Parser.builder().build();
    private final static HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();

    /**
     * Converts a markdown string to HTML
     *
     * @param markdown the markdown to convert
     * @return the converted HTML
     */
    public static String toHtml(String markdown) {
        if (StringUtils.isBlank(markdown))
            return "";

        Node node = PARSER.parse(markdown);

        String result = HTML_RENDERER.render(node);
        if (isSingleParagraph(result))
            result = StringUtils.stripEnd(StringUtils.stripStart(result, "<p>"), "</p>\n");

        return result;
    }

    private static boolean isSingleParagraph(String value) {
        boolean startsWith = StringUtils.startsWithIgnoreCase(value, "<p>");
        boolean endsWith = StringUtils.endsWithIgnoreCase(value, "</p>\n");
        boolean onlyOneStart = StringUtils.countMatches(value, "<p>") == 1;
        boolean onlyOneEnd = StringUtils.countMatches(value, "</p>") == 1;

        return startsWith && endsWith && onlyOneStart && onlyOneEnd;
    }
}
