package net.ozwolf.raml.apidocs.util;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

public class MarkDownHelper {
    private final static Parser PARSER = Parser.builder().build();
    private final static HtmlRenderer HTML_RENDERER = HtmlRenderer.builder().build();

    public static String toHtml(String markdown) {
        Node node = PARSER.parse(markdown);
        return HTML_RENDERER.render(node);
    }
}
