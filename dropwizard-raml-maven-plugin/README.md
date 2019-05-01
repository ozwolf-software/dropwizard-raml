# DropWizard RAML Maven Plugin

This Maven plugin is designed to generate a service's RAML specifications during the build process of the application.

## Dependency

### Maven

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-maven-plugin</artifactId>
    <version>2.1.0</version>
</dependency>
```

### Gradle

```gradle
compile 'net.ozwolf:dropwizard-raml-maven-plugin:2.1.0'
``` 

## Usage

### Goals

+ `generate` (default to `compile` phase)- Generate the RAML specification for the service.

### Properties

The RAML plugin takes three configuration properties:

+ `basePackage` - the base package of your application
+ `version` (optional, default `${project.version}`) - the application version the RAML specification will be built against
+ `outputFile` (optional, default `${basedir}/target/classes/apidocs/apidocs.raml`) - the file that will be created.  If this is changed to be outside of the `target/classes` path, obviously it won't be considered part of the compiled application.

### Example

```xml
<plugin>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-maven-plugin</artifactId>
    <version>2.1.0</version>
    <configuration>
        <basePackage>net.ozwolf.raml.example</basePackage>
    </configuration>
</plugin>
```