# DropWizard RAML Monitor

This library contains a DropWizard `Bundle` that can be added to your application with the goal of monitoring requests and responses to and from your services and confirming that they adhere to the RAML specification for the service.

The monitoring of this information happens asynchronously on a separate threadpool.  In the case of an error occurring during the monitoring, it will not interrupt the standard operation of the service.

## Dependency

### Maven

```xml
<dependency>
    <groupId>net.ozwolf</groupId>
    <artifactId>dropwizard-raml-monitor</artifactId>
    <version>2.0.0</version>
</dependency>
```

### Gradle

```gradle
compile 'net.ozwolf:dropwizard-raml-generator:2.0.0'
```

## Logging

The logger name used for reporting discrepencies between the service RAML specification and the realtime request/response payloads is called `raml-monitor`.  You can direct all output for RAML monitoring to it's own logging file by defining the logger specification (refer to [Configuration](https://www.dropwizard.io/1.3.8/docs/manual/core.html#id4) of the DropWizard documentation).

An example would be:

```yaml
logging:
  level: ERROR
  appenders:
    - type: file
      timeZone: Australia/Melbourne
      currentLogFilename: ./logs/my-app.log
      archivedLogFilenamePattern: ./logs/archives/my-app-%d.log.gz
      archivedFileCount: 30
   loggers:
    "raml-monitor":
      level: INFO
      - type: file
        timeZone: Australia/Melbourne
        currentLogFilename: ./logs/raml-monitor.log
        archivedLogFilenamePattern: ./logs/archives/raml-monitor-%d.log.gz
        archivedFileCount: 30
```

## What Is Reported?

The RAML monitor compares incoming requests to a resource method against the specified RAML and also compares the corresponding response values against the appropriate status codes.

Client error responses (ie. the `4xx` error code range) will have request validation errors ignored as the monitor is assuming that the service is appropriately responding to any invalid request with the appropriate client error.

## Validation Output

When a request/response pair generates validation errors, indicating that the behaviour of the service is not compliant with the service's own RAML specification, a formatted log message will be provided, similar in structor to the below:

```
[ /authors ]
  [ put->application/json->201 ]
    [ request ]
      [ security ]
        [ oauth2 ]
          [ headers ]
            [ authorization ]
              [ pattern ] - must match pattern [ Bearer (.+) ]
          [ body ]
            [ application/json ] - object has missing required properties (["name"])
    [ response ]
      [ 201 ]
        [ headers ]
          [ location ]
            [ required ] - must be provided
          [ body ]
            [ application/json ] - object has missing required properties (["bibliography"])
```

## Turning Off Monitoring

There is a certain overhead to monitoring the request and response payloads of a service and this can scale inline with the size of body contents for method calls.

You may decide you do not with to monitor certain resource methods (ie. long running processes, high volume endpoints or large request/response payloads).  This can be achieved by simply adding the `@DoNotMonitor` annotation to the resource or resource method.

**_Note:_** Any resource or resource method annotated with `@RamlIgnore` will be naturally ignored. 