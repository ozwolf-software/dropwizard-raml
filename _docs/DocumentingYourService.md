# Documenting Your Service

Documenting your service requires the [RAML Annotations](../dropwizard-raml-annotations) library, as the generation tool combines JAX-RS and Jackson annotations along with Morten Kjetland's [Jackson JSON Schema](https://github.com/mbknor/mbknor-jackson-jsonSchema) to provide greater control over the describing of your service.

## Describing Your Application

Every app needs to have a `@RamlApp` defined _somewhere_.  While it might seem prudent to place this on the DropWizard app itself, this annotation can become quite large when you include documentation, security schemes and trait descriptions.

Instead, it is recommended to have a companion class with the annotation, whose sole purpose is to provide a place for this header annotation to live.

An example can be found [here](../dropwizard-raml-example-service/src/main/java/net/ozwolf/raml/example/ExampleAppSpecs.java)

**`@RamlApp` Annotation**

| Property | Required | Default | Description |
| -------- | -------- | ------- | ----------- |
| title | yes | | the title of the application |
| description | yes |  | the description of the application |
| protocols | yes | `HTTPS` | the list of protocols the application can be connected to with |
| baseUri | yes | | the base URI that the service can be found one |
| documentation | no | | the list of documentation items for the RAML specification |
| security | no | | the list of security schemes the service supports |
| traits | no | | the list of traits that can be inherited | 

### Documentation

You can include as many pages of supporting documentation as you like to your RAML specification using the `documentation` property on the `@RamlApp` annotation, which takes a collection of `@RamlDocumentation` elements.

**`@RamlDocumentation` Annotation**

| Property | Description                                                                                     |
| -------- | ----------------------------------------------------------------------------------------------- |
| title    | the title of the documentation item                                                             |
| content  | the documentation content.  Can be either a reference to a resource file or the raw text itself |

### Security Schemes

The `@RamlApp` has a `security` property that accepts a collection of `@RamlSecurity` elements to help describe the types of security your application accepts.

A security scheme needs to adhere to the definitions defined [here](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md#security-schemes) by the RAML 1.0 specification.

If you are using either **OAuth 1.0** or **OAuth 2.0**, you will need to set the `oauth1Settings` or `oauth2Settings` property respectively on your `@RamlSecurity` entry.

A security entry can define request headers and query parameters as well as what responses may be encountered while using the security scheme.

The `key` value used on the scheme can then be used with the `@RamlSecuredBy` annotation to link resource methods to one or more security schemes.

### Traits

Traits are a way to define common behaviours within a single definition, making it easier to define such behaviour in multiple locations.

Using the `traits` property on the `@RamlApp` annotation, you can define a trait using a `@RamlTrait` entry.

Within this annotation, you can describe both request header and query parameters as well as expected responses.

All elements are optional.

## Parameters

All parameters, whether URI, header or query, can be described using the `@RamlParameter` annotation.

This annotation covers all properties for the different parameters of  

## Ignoring Elements

If you wish to have a resource class or method ignored as part of the generation process, simply add the `@RamlIgnore` annotation to it.

## Documenting A Resource

Resource classes are the base of individual resource paths.  The generator will scan within the given package and sub-packages for any class annotated with the `@Path` annotation and begin breaking it down into a resource definition.

Examples can be found [here](../dropwizard-raml-example-service/src/main/java/net/ozwolf/raml/example/resources/AuthorsResource.java) and [here](../dropwizard-raml-example-service/src/main/java/net/ozwolf/raml/example/resources/BooksResource.java).

### Resource Class

Resource classes with a `@Path` annotation must either have a `@RamlResource` or `@RamlIgnore` annotation.  The latter will exclude the resource class and it's methods and sub-resources from being included in the specification.

#### Example

```java
@RamlResource(displayName = "Authors", description = "manage the authors on record")
@Path("/authors")
public class AuthorsResource {
    // Class definition
}
```

If the `@RamlResource` annotation is not found, an error will be raised.

### Sub-Resources

Sub resources are collections of paths within a resource class that come under the resource's main path.  For example, a `@Path("/{id}")` within the resource class indicates a sub-resource.

Sub-resource information, such as a description and the URI parameter definitions, are made on the resource class using the `@RamlSubResources` annotation.

#### Example

```java
@RamlResource(displayName = "Authors", description = "manage the authors on record")
@RamlSubResources(
        @RamlSubResource(path = @Path("/latest"), description = "resource to manage latest authors")
)
@Path("/authors")
public class AuthorsResource {
    // Class definition
}
```

### URI Parameters

Because URI parameters are relevant to resources and sub-resources and not specifically to a resource method, these are defined on the resource class itself.

If a resource or sub-resource are found to have URI parameter placeholders in their `@Path` annotation but no definition is found, an error will be raised.

To document the URI parameters associated with a resource class, use the `@RamlUriParameters` annotation.  For documenting URI parameters associated with sub-resources, the `@RamlSubResource` annotation has a `uriParameters` property that can be set.

#### Example
```java
@RamlResource(displayName = "Authors", description = "manage the authors on record")
@RamlSubResources(
        @RamlSubResource(
                path = @Path("/books/{bookId}"), 
                description = "resource to manage latest authors", 
                uriParmateters = @RamlParameter(name = "bookId", description = "a book identifier", type = "integer")
        )
)
@RamlUriParameters(
        @RamlParameter(name = "id", description = "an author identifier", type = "integer")
)
@Path("/authors/{id}")
public class AuthorResource {
    // Class definition
}
```

## Documenting A Resource Method

A resource method is any method implementing the `@GET`, `@POST`, `@PUT`, `@PATCH` or `@DELETE` annotations.

The RAML generator will automatically create method entries for the RAML on any method that is annotated with the afore-mentioned annotations.  It will make it's best effort to divine the inputs and outputs of the resource method using JAX-RS annotations such as `@Consumers`, `@Produces`, `@QueryParam` and `@HeaderParam`.

Of course, the documentation around your resource method will be a lot better if the RAML annotations relevant to that method are used to enrich it.

### Description

Your resource method can be given a description using the `@RamlDescription` annotation.

### Query Parameters

Query parameters can be defined and described in two ways.