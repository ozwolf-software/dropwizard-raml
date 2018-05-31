package net.ozwolf.raml.example;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class ExampleServiceApp extends Application<ExampleServiceAppConfiguration> {
    @Override
    public String getName() {
        return "dropwizard-raml-example-service";
    }

    @Override
    public void run(ExampleServiceAppConfiguration configuration, Environment environment) throws Exception {

    }
}
