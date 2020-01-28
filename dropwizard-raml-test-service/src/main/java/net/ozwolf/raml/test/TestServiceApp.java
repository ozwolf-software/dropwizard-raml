package net.ozwolf.raml.test;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.test.core.exception.ConstraintViolationExceptionMapper;
import net.ozwolf.raml.test.core.exception.UnhandledExceptionMapper;
import net.ozwolf.raml.test.core.exception.WebApplicationExceptionMapper;
import net.ozwolf.raml.test.jackson.BigDecimalModule;
import net.ozwolf.raml.test.resources.AuthorsResource;
import net.ozwolf.raml.test.resources.BooksResource;

public class TestServiceApp extends Application<TestServiceAppConfiguration> {
    @Override
    public String getName() {
        return "dropwizard-raml-test-service";
    }

    @Override
    public void initialize(Bootstrap<TestServiceAppConfiguration> bootstrap) {
        bootstrap.getObjectMapper().registerModule(new BigDecimalModule());
    }

    @Override
    public void run(TestServiceAppConfiguration configuration, Environment environment) {
        environment.jersey().register(new WebApplicationExceptionMapper());
        environment.jersey().register(new ConstraintViolationExceptionMapper());
        environment.jersey().register(new UnhandledExceptionMapper());

        environment.jersey().register(new AuthorsResource());
        environment.jersey().register(new BooksResource());
    }

    public static void main(String[] args) throws Exception {
        new TestServiceApp().run(args);
    }
}
