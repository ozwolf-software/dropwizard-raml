# DropWizard RAML Generator

This library contains a `RamlGenerator` class that will generate a RAML specification for an application based on a combination of toolbox and JAX-RS annotations.

## Schema and Example Generation

Currently, the generator only supports schema and example generation for `application/json` and `text/xml` content.

If your resources return other content types, then resource files will need to be supplied manually.

## Example Usage

```java
try {
    String raml = new RamlGenerator("com.test.app", "1.2.3").generate();
} catch(RamlGenerationException e){}
    LOGGER.error("Error generating RAML specification.", e);
}
```