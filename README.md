# DropWizard RAML Toolbox

A collection of tools that originally started as a way to better learn how [RAML](https://raml.org) and JAX-RS could integrate together (before MuleSoft released the [RAML for JAX-RS](https://github.com/mulesoft-labs/raml-for-jax-rs) project.

It has evolved into a collection of annotations and tools for utilising RAML API documentation with a DropWizard service.

Some libraries, while part of this toolbox aimed at enhancing DropWizard, can be used independently or with other JAX-RS applications.

## What is RAML?

RAML is an API specification language developed by [MuleSoft](https://www.mulesoft.com/) based on YAML.  

It is a language for the definition of HTTP-based APIs that embody most or all of the principles of Representational State Transfer (REST). This document constitutes the RAML specification, an application of the YAML 1.2 specification. The RAML specification provides mechanisms for defining practically-RESTful APIs, creating client/server source code, and comprehensively documenting the APIs for users.

For more information on how RAML works, please refer to the following links:

+ [RAML 1.0 Specification](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md/)
+ [RAML 100 Tutorial](https://raml.org/developers/raml-100-tutorial)
+ [RAML 200 Tutorial](https://raml.org/developers/raml-200-tutorial)

## Components

+ [dropwizard-raml-annotations](./dropwizard-raml-annotations) - a library containing the RAML annotations used to help describe your service in code
+ [dropwizard-raml-generator](./dropwizard-raml-generator) - the library that contains the logic to investigate your project and derive a RAML specification
+ dropwizard-raml-maven-plugin (TBA) - a Maven plugin that can be used to generate a RAML specification file during the build process
+ dropwizard-raml-view (TBA) - a configured bundle that provides a readable endpoint on services explaining the API
+ dropwizard-raml-monitor (TBA) - a runtime library that monitors incoming and outgoing requests, logging if successful request/response chains do not adhere to the API specification
+ dropwizard-raml-tester (TBA) - a testing library that allows DropWizard testing to verify RAML specifications