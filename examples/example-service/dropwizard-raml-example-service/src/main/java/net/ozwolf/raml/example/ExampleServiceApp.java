package net.ozwolf.raml.example;

import com.google.common.io.Resources;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.apidocs.ApiDocsBundle;
import net.ozwolf.raml.apidocs.util.PropertyUtils;
import net.ozwolf.raml.example.resources.AuthorsResource;
import net.ozwolf.raml.example.resources.BooksResource;
import net.ozwolf.raml.example.resources.filters.SecurityFilter;
import net.ozwolf.raml.example.core.exception.WebApplicationExceptionMapper;
import net.ozwolf.raml.example.core.exception.UnhandledExceptionMapper;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

public class ExampleServiceApp extends Application<ExampleServiceAppConfiguration> {
    private final static AtomicReference<byte[]> SAMPLE_PDF = new AtomicReference<byte[]>();

    @Override
    public String getName() {
        return "dropwizard-raml-example-service";
    }

    @Override
    public void initialize(Bootstrap<ExampleServiceAppConfiguration> bootstrap) {
        bootstrap.addBundle(new ApiDocsBundle("net.ozwolf.raml.example", PropertyUtils.getValueFrom("application.properties", "version")));
    }

    @Override
    public void run(ExampleServiceAppConfiguration configuration, Environment environment) {
        environment.jersey().register(new WebApplicationExceptionMapper());
        environment.jersey().register(new UnhandledExceptionMapper());

        environment.jersey().register(new AuthorsResource());
        environment.jersey().register(new BooksResource());
        environment.jersey().register(new SecurityFilter.Feature());
    }

    public static void main(String[] args) throws Exception {
        new ExampleServiceApp().run(args);
    }

    public static byte[] getSamplePDF() {
        if (SAMPLE_PDF.get() == null) {
            try {
                URL resource = ExampleServiceApp.class.getClassLoader().getResource("pdf/pdf-sample.pdf");
                if (resource == null)
                    throw new IllegalStateException("Missing sample PDF file.");

                SAMPLE_PDF.set(Resources.toByteArray(resource));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return SAMPLE_PDF.get();
    }
}
