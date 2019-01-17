# Change Log

## 2.0.0

+ Added ability to validate requests and responses against a RAML specification.
+ Added ability to monitor incoming and outgoing traffic from a DropWizard service and report when there are inconsistencies with the specification.
+ Fixed when the API docs bundle loads it's RAML specifications so as error logs are consistently generated at startup.
+ Modified way URI parameters are defined for sub-resources.

## 1.1.1

+ Fixed sub-resources including path parameters from parent resources.

## 1.1.0

+ Added ability to flag a return type as a collection.

## 1.0.3

+ Fixed element identifiers in Freemarker templates.

## 1.0.2

+ Reverted Scala version to more commonly-used `2.11.x`. 

## 1.0.1

+ Fixed Null Pointer Exception errors when extracting 0.8 RAML specifications.

## 1.0.0

Initial release of project.  Contains the following features:

+ Ability to generate RAML specifications from annotated source code.
+ Ability to present RAML specifications in human-readable webpage via the `/apidocs` endpoint.
  