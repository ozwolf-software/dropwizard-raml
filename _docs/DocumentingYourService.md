# Documenting Your Service

The DropWizard RAML project uses a combination of JAX-RS + Jackson annotations with the [RAML Annotations](../dropwizard-raml-annotations) library to document your application's code base.

An example of a documented service can be found in the [Example Service Project](../dropwizard-raml-example-service).

### JSON Schema Documentation

In conjunction with Jackson's own JSON annotations, the project also uses Morten Kjetland's [Jackson JSON Schema](https://github.com/mbknor/mbknor-jackson-jsonSchema) project to provide more JSON schema documentation.

## Describing Your Application

### `@RamlApp`

The `@RamlApp` annotation is used to provide the description information for your application.  This annotation needs to exist once (and once only) within the base package and sub-packages defined when the specification document gets generated.

This annotation _does not_ need to reside on your application class and due to the size it can reach, we recommend placing it on a supporting class.  You can see such usage [here](../dropwizard-raml-example-service/src/main/java/net/ozwolf/raml/example/ExampleAppSpecs.java).

| Property | Required | Default | Description |
| -------- | :------: | :-----: | ----------- |
| title | yes |  | The title of your application |
| description | no | | The description of your application |
| protocols | yes | `HTTPS` | The protocols your application can be connected on |
| baseUri | yes | | The base URI of the application |
| documentation | no | | The collection of documentation pages defined using the `@RamlDocumentation` annotation
| security | no | | The collection of security schemes your application supports using the `@RamlSecurity` annotation
| traits | no | | The collection of traits your application's resource methods can inherit from |

#### `@RamlDocumentation`

The `documentation` property on the `@RamlApp` annotation can accept a list of `@RamlDocumentation` annotations.  This annotation provides the RAML specification with a collection of documentation pages to be referenced.

| Property | Required | Description |
| -------- | :------: | ----------- |
| title | yes | The title of the documentation page |
| content | yes | The content of the documentation page.  Can be a reference to a resource file _or_ the content directly. |

#### `@RamlSecurityScheme`

The `security` property on the `@RamlApp` annotation can accept a list of `@RamlSecurityScheme` annotations.  This annotation provides a description of supported security schemes that your application uses.

This collection is used in combination with the `@RamlSecuredBy` annotation to bind resource methods to the associated security schemes.

| Property | Required | Description |
| -------- | :------: | ----------- |
| key | yes | The key the security scheme will be referenced by in the `@RamlSecuredBy` annotation |
| type | yes | The security scheme type.  Must adhere to the types defined [here](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md#security-schemes) by the RAML 1.0 specification. |
| description | no | The description of the security scheme. |
| describedBy | yes | The system descriptor of the security scheme's behaviour.  See the `@RamlDescribedBy` annotation. |
| oauth1Settings | yes if type is `OAuth 1.0` | The settings related to OAuth 1.0 security type |
| oauth2Settings | yes if type is `OAuth 2.0` | The settings related to OAuth 2.0 security type |

#### `@RamlTrait`

The `traits` property on the `@RamlApp` annotation can accept a list of `@RamlTrait` annotations.  This annotation is used to describe traits that resource methods can inherit behaviour from.

This collection is used in combination with the `@RamlIs` annotation to attach trait behaviour to resource methods.

| Property | Required | Description |
| -------- | :------: | ----------- |
| key | yes | The key the trait will be referenced by in the `@RamlIs` annotation |
| usage | no | A usage description of the trait |
| description | no | The description of the trait |
| describedBy | yes | The system descriptor of the trait's behaviour.  See the `@RamlDescribedBy` annotation. |

#### `@RamlDescribedBy` 

Both the `@RamlSecurityScheme` and `@RamlTrait` annotations to describe their request/response behaviour and properties.

| Property | Required | Description |
| -------- | :------: | ----------- |
| headers | no | The collection of request header parameters.  Uses the `@RamlParameter` annotation to define the parameters. |
| queryParameters | no | The collection of request query parameters.  Uses the `@RamlParameter` annotation to define the parameters. |
| responses | no | The collection of responses that can be returned.  Uses the `@RamlResponse` annotation to define the responses. |

## Common Annotations

### `@JsonIgnore`

Any resource class or method with the `@JsonIgnore` annotation will be excluded for documentation purposes.

### `@RamlParameter`

The `@RamlParameter` annotation is used to define a URI, header or query parameter as per the RAML 1.0 specifications.

| Property | Required | Default | Description |
| -------- | :------: | :-----: | ----------- |
| name | yes | | The name of the parameter (as it will be used in actual requests or URI templates) |
| type | yes | | The type of parameter. Must adhere to the types defined [here](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md#built-in-types).  If the parameter is an `array` of type, define the type here.  Flagging the `multiple` property as true will ensure the parameter gets defined as an `array` of `type` |
| displayName | no | | The display name of the parameter |
| description | no | | The description of the parameter |
| required | no | true | The flag indicating if the parameter is compulsory or not. |
| multiple | no | false | The flag indicating if the parameter is multi-value. |
| allowedValues | no | | The list of allowed values.  If `allowedValuesEnum` is defined, this property is ignored. |
| allowedValuesEnum | no | | A reference to a `Enum` class that represents the list of allowed values. |
| example | no | | An example value of the parameter. |
| pattern | no | | A regular expression pattern the parameter value must adhere to.  This is ignored for non-string types. |
| minimum | no | | The minimum value of the parameter (as a `long`).  This is ignored for non-numeric types. |
| maximum | no | | The maximum value of the parameter (as a `long`).  This is ignored for non-numeric types. |

### `@RamlResponse`

The `@RamlResponse` annotation is used to describe a response related to a status.  This annotation is used by the `@RamlDescribedBy` annotation within the security schemes and traits as well as by the `@RamlResponses` annotation on a resource method.  A response consists of a status code, description and collection of available body entities.

| Property | Required | Description |
| -------- | :------: | ----------- |
| status | yes | The status code of this response |
| description | no | A description of what this response represents |
| bodies | yes | A collection of `@RamlBody` definitions related to the response |

### `@RamlBody`

The `@RamlBody` annotation is used to describe a body entity for a specific content type.  With this annotation you specify a content type and can either reference a class type to generate schemas and examples from or directly reference resources/text for these.

| Property | Required | Description |
| -------- | :------: | ----------- |
| contentType | yes | The content type of the body payload |
| type | no | The body class reference (either request or response type) |
| schema | no | A reference to a resource file or direct text specifying the body schema.  Ignored if `type` is defined. |
| schema | no | A reference to a resource file or direct text providing a body example.  Ignored if `type` is defined. |

#### `@RamlExample` Usage

The `@RamlExample` usage can be placed on any static method for a response type and the generation process will use that static method to instantiate an instance of the type and use the appropriate Jackson `ObjectMapper` to generate an example payload.

The method this annotation is attached to must be a zero-argument `static` method.

#### Supported Media Types

The generation process currently supports the following media types:

| Media Type | Schema Generation | Example Generation | Notes |
| ---------- | :---------------: | :----------------: | ----- |
| application/json | yes | yes | |
| text/xml | no | yes | Currently, there is no tooling for an XSD to be easily derived from a Jackson-annotated class |

## Documenting Your Resources

Resources are the bread and butter of your application.  This section onwards will look at how you will document resources using a combination of JAX-RS annotations and the RAML annotations project.

The generation process will assume that any class with a `@Path` annotation is a base resource location unless that class has the `@RamlIgnore` annotation on it.

### `@RamlResource`

Any resource class with a `@Path` annotation also requires a `@RamlResource` annotation on it.

| Property | Required | Description |
| -------- | :------: | ----------- |
| displayName | yes | The display name for the resource |
| description | yes | A meaningful description for the resource |

### `@RamlUriParameters`

If the `@Path` annotation on a resource class includes a URI parameter in it's template, the class will also require the `@RamlUriParameters` annotation, describing those path parameters for the resource.  The parameter takes one or more `@RamlParameter` annotations.

### `@RamlSubResources`

A resource class can contain sub-resources.  For example, the resource could have a `@Path("/authors")` annotation on it with methods within the class having their own `@Path` annotations (eg. `@Path("/{id}")` indicating a sub-resource path of `/authors/{id}`).  Because sub-resource paths can be shared across multiple methods, sub-resources need to be defined through the use of the `@RamlSubResources` annotation on the resource class itself.

This annotation takes on or many `@RamlSubResource` annotations.

#### `@RamlSubResource`

This annotation is used to describe a single sub-resource that will be found inside the resource class.

| Property | Required | Description |
| -------- | :------: | ----------- |
| path | yes | The `@Path` annotation matching the sub resource method(s) in the resource |
| description | no | A meaningful description for the sub resource |
| uriParameters | no | A collection of `@RamlParameter` definitions for any URI parameters on the sub-resource |

## Documenting A Resource Method

A resource method is any method within a resource class annotated with one of the following JAX-RS annotations:

+ `@GET`
+ `@DELETE`
+ `@POST`
+ `@PUT`
+ `@PATCH`

If a method inside the resource does not have one of the above annotations or has the `@RamlIgnore` annotation, it will not be included in the specification.

### `@RamlDescription`

A meaningful description of what the method is for can be supplied via the use of the `@RamlDescription` annotation on the method.

### `@RamlSecuredBy`

The `@RamlSecuredBy` annotation can be used to bind the resource method to one or more security schemes defined by the `@RamlApp.security` property.

### `@RamlIs`

This annotation can be used to inherit specific traits defined by the `@RamlApp.traits` property.

### Headers and Query Parameters

There are two ways to define headers and query parameters on a resource.  They can be defined explicitly via the `@RamlHeaders`, `@RamlQueryParameters` and `@RamlFormParameters` annotations.  These annotations take one or many `@RamlParameter` definitions.

If these annotations are used on a method, the generation process will not attempt to describe any of the respective parameter types by discovery.  This means if your annotation contains a different parameter set to what is actually accepted by the resource, the difference will not be reported on.

The explicit approach is ideal if there are parameters consumed by the request through filters or manual request processing but are not explicitly parameters within the method definition of the code.

#### Implied Documentation

If the `@RamlHeaders`, `@RamlQueryParameters` and/or `@RamlFormParameters` annotations are not present on a resource method, the generation process will implicitly look for parameters with the `@HeaderParam` or `@QueryParam` annotations.

The discovery will detect `Collection` parameters and appropriately map them as `array` of `type` parameters, using generic declaration discovery to determine the internal type.

If the type is an `Enum`, the allowed values will be defined for the parameter.

The following annotations attached to a parameter will help drive out further documentation:

| Annotation | Description |
| `@RamlDescription` | Adds a meaningful description to the parameter |
| `@RamlExample` | Provides an example value for the parameter |
| `@javax.validation.constraints.NotNull` | Flags the parameter as being required |
| `@javax.ws.rs.DefaultValue` | Sets a default value for the parameter |
| `@javax.validation.constraints.Pattern` | Defines a regular expression pattern the parameter must adhere to |
| `@javax.validation.constraints.Min` | Defines a minimum numeric value the parameter must be greater than |
| `@javax.validation.constraints.Max` | Defines a maximum numeric value the parameter must be less than |

### `@RamlRequests`

Request bodies are defined via the `@RamlRequests` annotation.  This annotation takes one or many `@RamlBody` annotations.

If no `@RamlRequests` annotation is found on a method with a request parameter associated with it, a generation error will occur.

### Responses

Documenting responses for a resource method is best achieved through the `@RamlResponses` annotation, which takes one or many `@RamlResponse` annotations.  If however this annotation isn't present, the generation process will assume a `200 OK` response code and try to generate a response definition from a combination of then `@Produces` annotation (defaulting to `application/json` if not present) and the return type.

If the return type of the method is a Jackson-annotated object with a `@RamlExample` annotation present, it will create schemas and examples automatically.

The `@RamlResponses` annotation allows better control over defining the status codes being returned.