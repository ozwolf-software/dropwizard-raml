# DropWizard RAML Example Service

This service is designed to be loaded into an IDE and run to show how the API documentation system works.

Load the `pom.xml` for the example service and let the IDE complete the dependency import.

Then run the service with `net.ozwolf.raml.example.ExampleServiceApp server conf/dropwizard-raml-example-service.yml`.  This will start the service on port `9000`.

You can then view the RAML API specification via the `http://localhost:9000/apidocs` endpoint.