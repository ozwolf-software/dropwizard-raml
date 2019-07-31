package net.ozwolf.raml.generator.util;

import net.ozwolf.raml.generator.RamlMedia;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RamlMediaRule implements TestRule {
    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RamlMedia.initialize(null);
                try {
                    statement.evaluate();
                } finally {
                    RamlMedia.destroy();
                }
            }
        };
    }
}
