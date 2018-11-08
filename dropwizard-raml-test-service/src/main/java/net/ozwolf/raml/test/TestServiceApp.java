package net.ozwolf.raml.test;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.test.resources.AuthorsResource;
import net.ozwolf.raml.test.resources.BooksResource;

public class TestServiceApp extends Application<TestServiceAppConfiguration> {
    @Override
    public String getName() {
        return "dropwizard-raml-test-service";
    }

    @Override
    public void initialize(Bootstrap<TestServiceAppConfiguration> bootstrap) {

    }

    @Override
    public void run(TestServiceAppConfiguration configuration, Environment environment) {
        environment.jersey().register(new AuthorsResource());
        environment.jersey().register(new BooksResource());
    }

    public static void main(String[] args) throws Exception {
        new TestServiceApp().run(args);
    }
}
