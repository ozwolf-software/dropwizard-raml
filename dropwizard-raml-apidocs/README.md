# DropWizard API Docs Bundle

This library contains a DropWizard `Bundle` that can be added to your service with the goal of providing a human-readable endpoint for describing your API specification.

The bundle will log errors and and return `503 Service Unavailable` errors if there is an issue trying to initialize the bundle.

It will not cause the application from starting.

**Note**: Due to current limitations in Mulesoft's JVM libraries, the bundle is currently unable to provide a raw dump of the RAML specification at runtime.

## Dependency

### Maven

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-apidocs</artifactId>
    <version>1.1.1</version>
</dependency>
```

### Gradle

```gradle
compile 'net.ozwolf:dropwizard-raml-generator:1.1.1'
```

## Example Usage

### Runtime Package Scanning

```java
public class MyApp extends Application<MyAppConfiguration> {
    @Override
    public String getName() {
        return "my-app";
    }
    
    @Override
    public void initialize(Bootstrap<MyAppConfiguration> bootstrap){
        bootstrap.addBundle(new ApiDocsBundle("com.my.app", PropertyUtils.getValueFrom("application.properties", "version")));
    }
}
```

### Existing RAML Specification File

```java
public class MyApp extends Application<MyAppConfiguration> {
    @Override
    public String getName() {
        return "my-app";
    }
    
    @Override
    public void initialize(Bootstrap<MyAppConfiguration> bootstrap){
        bootstrap.addBundle(new ApiDocsBundle("apidocs/apidocs.yml"));
    }
}
```

## Viewing API Specifications

Your API specifications can then be viewed via the `/apidocs` resource on your application's operations pot.