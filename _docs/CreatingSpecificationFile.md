# Creating A Specification File

One of the strengths of RAML is that your API specifications can be described in a YAML file and distributed to consumers who can then build integrations and test cases that align with your specification.

If you've used the [RAML Annotations](../dropwizard-raml-annotations) library to document your service in code, there are two ways you can generate a valid RAML specification.

## Maven Plugin

The DropWizard RAML project includes a [Maven Plugin](../dropwizard-raml-maven-plugin) library that can be used as part of the build project.  This plugin will use the generator library to create a RAML specification file.

This plugin is attached to the compile lifecycle of the Maven build process and will create a file under the `apidocs/apidocs.yml` path within the compiled classpath.

The plugin has three properties that can be set:

+ `basePackage` - The base package the application's source code is under.
+ `version` - The application version.  This defaults to Maven's `${project.version}` unless otherwise specified.
+ `outputFile` - The location the specification file will be outputted to.  This is defaulted to `${basedir}/target/classes/apidocs/apidocs.yml` so as it will be compiled into the application's classpath for later reference.

## RAML Generator

If the Maven plugin is not to your tasting, the DropWizard RAML project also has a [RAML Generator](../dropwizard-raml-generator) library that you can use to generate a RAML specification through your own means.

This is the library used by the above plugin plus the other tools to derive a RAML specification from your source code.  You can use this library to build your own toolset for generating RAML.