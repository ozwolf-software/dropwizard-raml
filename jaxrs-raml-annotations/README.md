# DropWizard RAML Annotations

A library to provide a common set of annotations to help extend the describing of a Jersey + Jackson JAX-RS service with extra descriptors, security schemes and traits.

This library does not specifically need DropWizard.

## Dependency

### Maven

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-annotations</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```gradle
compile 'net.ozwolf:dropwizard-raml-annotations:1.0.0'
```

## Annotations

### `RamlApp`

There _must be exactly 1_ `@RamlApp` annotation within the supplied package and it's sub-packages.  This annotation is used to provide the header information of the RAML specification 