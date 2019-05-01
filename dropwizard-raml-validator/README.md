# DropWizard RAML Validator

This library contains an API validator that will accept information about a HTTP request and validate the inputs and outputs against a given RAML specification.

## Dependency

### Maven

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-validator</artifactId>
    <version>2.1.0</version>
</dependency>
```

### Gradle

```gradle
compile 'net.ozwolf:dropwizard-raml-validator:2.1.0'
```

## Example Usage

```java
RamlGenerator generator = new RamlGenerator("my.app.package", "1.2.3");
String raml = generator.generate();

RamlModelResult model = new RamlModelBuilder().build(raml, "/");

RamlApplication app = model.isVersion10() ? new V10_RamlApplication(model.getApiV10()) : new V08_RamlApplication(model.getApiV08());

RamlResource resource = app.find("/path/to/resource", "/and/sub-resource").orElse(null);
if (resource == null) {
    // Handle unknown resource
}

RamlMethod method = resource.find("POST").orElse(null);
if (method == null){
    // Handle unknown method 
}

ApiRequest request = new ApiRequest.Builder(resource, method, "/path/to/resource/and/sub-resource", MediaType.APPLICATION_JSON_TYPE)
    .withHeaders(<headers>)
    .withPathParameters(<pathParameters>)
    .withQueryParameters(<queryParameters>)
    .withContent(<byteContent>)
    .build();

ApiResponse response = new ApiResponse.Builder(method, 200, MediaType.APPLICATION_JSON_TYPE)
    .withHeaders(<headers>)
    .withContent(<byteContent>)
    .build();

List<SpecificationViolation> violations = new ApiValidator().validate(request, response);
if (!violations.isEmpty()){
    LOGGER.error(new SpecificationViolationException(violations));
}
```