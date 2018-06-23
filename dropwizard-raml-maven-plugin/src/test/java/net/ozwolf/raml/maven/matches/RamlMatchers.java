package net.ozwolf.raml.maven.matches;

import org.assertj.core.api.Condition;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;

public class RamlMatchers {
    public static Condition<String> validRaml(){
        return new Condition<String>(){
            @Override
            public boolean matches(String value) {
                RamlModelResult model = new RamlModelBuilder().buildApi(value, "/");
                return !model.hasErrors();
            }
        };
    }
}
