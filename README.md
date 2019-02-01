# DropWizard RAML

[![Travis](https://img.shields.io/travis/ozwolf-software/dropwizard-raml.svg?style=flat-square)](https://travis-ci.org/ozwolf-software/dropwizard-raml)
[![Maven Central](https://img.shields.io/maven-central/v/net.ozwolf/dropwizard-raml.svg?style=flat-square)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.ozwolf%22%20AND%20a%3A%22dropwizard-raml%22)
[![Apache 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](LICENSE)

This project started as a way to better learn how [RAML](https://raml.org) and JAX-RS could integrate together (before MuleSoft released the [RAML for JAX-RS](https://github.com/mulesoft-labs/raml-for-jax-rs) project).

During this time, this private project expanded with tools and plugins that were suitable for integrating RAML specification generating and usage with a DropWizard micro service.

## DropWizard Compatibility

+ `1.0.0+` - DropWizard 1.3+

## Documentation

Please refer to the [wiki](https://github.com/ozwolf-software/dropwizard-raml/wiki) for information on using the tools in this project.

**Note:** The documentation of this project is an ongoing task.  If you feel something is fundamentally missing from it, please raise an issue with a suggestion.

## Components

+ [Annotations](./dropwizard-raml-annotations) - a library containing the RAML annotations used to help describe a JAX-RS service in code
+ [RAML Generator](./dropwizard-raml-generator) - the library that contains the logic to investigate your project and derive a RAML specification
+ [Maven Plugin](./dropwizard-raml-maven-plugin) - a Maven plugin that can be used to generate a RAML specification file during the build process
+ [API Docs](./dropwizard-raml-apidocs) - a configured bundle that provides a readable endpoint on services explaining the API
+ [Validator](./dropwizard-raml-validator) - a validation library that confirms a HTTP request against a RAML specification 
+ [Monitor](./dropwizard-raml-monitor) - a runtime library that monitors incoming and outgoing requests, logging if successful request/response chains do not adhere to the API specification
+ Tester (TBA) - a testing library that allows DropWizard testing to verify RAML specifications
+ [Example Service](./examples/example-service/dropwizard-raml-example-service) - an example DropWizard JAX-RS service showcasing all the tools available in this project

## Scala Dependency

This project has a dependency on `org.scala-lang:scala-library:2.11.*`.  As a transient depdendency, this may cause conflicts with other libraries.

## Using In Non-DropWizard Jersey JAX-RS Projects

**Note**: There are plans to remove DropWizard as a dependency from components that don't fundamentally _require_ it for it's role (ie. the RAML generator).

The [Annotations](./dropwizard-raml-annotations) and [RAML Generator](./dropwizard-raml-generator) components _can_ be used in non-DropWizard Jersey JAX-RS projects that utilise Jackson.

This would allow a RAML specification to be generated from such a service.

To achieve this, specific dependencies will need to be provided for the components.  The dependencies are provided below.  While no guarantee is provided, it should _theoretically_ work.  YMMV.

### Dependencies

+ **Annotations**
    + `javax.ws.rs:javax.ws.rs-api:2.0.1+`
+ **RAML Generator**
    + `com.fasterxml.jackson.core:jackson-core:2.9.5+`
    + `com.fasterxml.jackson.core:jackson-databind:2.9.5+`
    + `com.fasterxml.jackson.core:jackson-annotations:2.9.5+`
    + `com.fasterxml.jackson.datatype:jackson-datatype-joda:2.9.5+`
    + `com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.5+`
    + `javax.ws.rs:javax.ws.rs-api:2.0.1+`
    + `org.glassfish.jersey.core:jersey-common:2.25.1+`
    + `org.apache.commons:commons-lan3:3.5+`
