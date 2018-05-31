package net.ozwolf.raml.example;

import net.ozwolf.raml.annotations.RamlApp;

@RamlApp(
        title = "DropWizard Example App",
        description = "An example application documented using the RAML annotations.",
        protocols = "HTTP",
        baseUri = "http://localhost:8888",
        documentation =
)
public final class ExampleAppSpecs {
}
