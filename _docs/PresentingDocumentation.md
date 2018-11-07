# Presenting API Documentation

While generating a RAML specification has certain benefits from a machine level (such as being able to use a specification file to setup requests through tools like POSTman), it is always helpful if developers can view your API specifications in a human-readable format to help develope against.

This is achieved via the [RAML API Docs](../dropwizard-raml-apidocs) library.  This library provides a DropWizard bundle that will add a `/apidocs` resource to your service where developers can view your specifications in a human-readable format.

## Using Annotations

If you are using the [RAML Annotations](../dropwizard-raml-annotations) to document your service, you can simply point the bundle at your base package and provide a version number at runtime.

While this will add a small startup cost to your application as it uses Reflection to scan your classpath for annotations and generate the documentation, it allows for runtime-based generation without the need to run any external build processes to generate the appropriate specification file.

### Application Version

Because application versions are fluid, it might not be conducive to hard-code the version number into the source code.  The RAML API docs library provides a `PropertyUtils` class that can help.

What we recommend is having a properties file in your app's resources with a property like `version=${project.version}` (assuming Maven in this example).  When the app compiles, the properties file will then be compiled into the classpath with the actual app version defined in it.

You can then use the utils class to have `String version = PropertyUtils.getValueFrom("application.properties", "version")` which will look for the `version` property in the `application.properties` file on your classpath.

### Example Usage

Below is an example usage of setting up the API docs bundle using JIT runtime documentation.

```java
public class MyApplication extends Application<MyApplicationConfiguration> {
    // Usual DropWizard stuff
    
    @Override
    public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap){
        bootstrap.addBundle(new ApiDocsBundle("com.my.app", PropertyUtils.getValueFrom("application.properties", "version")));
    }
}
```

## Using A Specification File

If you have an existing RAML specification file (in either 0.8 or 1.0 format) or you are using the Maven plugin to generate the file at compile time, you can register the bundle making reference to this file on your classpath.

The RAML specification file _needs_ to be on your classpath.

### Example Usage

Below is an example usage of setting up the API docs bundle using an existing RAML specification file.

```java
public class MyApplication extends Application<MyApplicationConfiguration> {
    // Usual DropWizard stuff
    
    @Override
    public void initialize(Bootstrap<MyApplicationConfiguration> bootstrap){
        bootstrap.addBundle(new ApiDocsBundle("apidocs/apidocs.yml"));
    }
}
```