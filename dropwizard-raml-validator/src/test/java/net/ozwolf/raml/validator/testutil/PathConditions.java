package net.ozwolf.raml.validator.testutil;

import net.ozwolf.raml.validator.Node;
import org.mockito.ArgumentMatcher;

public class PathConditions {
    public static ArgumentMatcher<Node> isPathOf(String path) {
        return p -> p.toString().equals(path);
    }
}
