# DropWizard RAML

This project started as a way to better learn how [RAML](https://raml.org) and JAX-RS could integrate together (before MuleSoft released the [RAML for JAX-RS](https://github.com/mulesoft-labs/raml-for-jax-rs) project).

During this time, this private project expanded with tools and plugins that were suitable for integrating RAML specification generating and usage with a DropWizard micro service.

## Documentation

Please refer to the [wiki](./_docs) for information on using the tools in this project.

## Components

+ [Annotations](./dropwizard-raml-annotations) - a library containing the RAML annotations used to help describe a JAX-RS service in code
+ [RAML Generator](./dropwizard-raml-generator) - the library that contains the logic to investigate your project and derive a RAML specification
+ [Maven Plugin](./dropwizard-raml-maven-plugin) - a Maven plugin that can be used to generate a RAML specification file during the build process
+ [HTML View](./dropwizard-raml-html) - a configured bundle that provides a readable endpoint on services explaining the API
+ [Monitor](./dropwizard-raml-monitor) - a runtime library that monitors incoming and outgoing requests, logging if successful request/response chains do not adhere to the API specification
+ [Tester](./dropwizard-raml-tester) - a testing library that allows DropWizard testing to verify RAML specifications
+ [Example Service](./dropwizard-raml-example-service) - an example DropWizard JAX-RS service showcasing all the tools available in this project

## Using In JAX-RS Projects

This project has been built on a number of assumptions around dependencies and libraries provided by the DropWizard framework.  While certain components are (to a certain degree) agnostic of DropWizard and would theoretically work with a JAX-RS + Jackson combination, the dependency tree is fleshed out.

While no guarantee is given for certain libraries to work 100%, as long as the transient dependencies they inherit from DropWizard are available (eg. JAX-RS API, Jackson modules, dataformats, etc), they should _theoretically_ work.  YMMV.