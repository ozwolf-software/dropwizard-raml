package net.ozwolf.raml.generator.testapp;

import net.ozwolf.raml.annotations.*;

import javax.ws.rs.core.MediaType;

@RamlApp(
        title = "Test App",
        description = "Test application to check the RAML creation is correct",
        baseUri = "https://test-app.test.com",
        documentation = {
                @RamlDocumentation(title = "Summary", content = "apispecs/documentation/summary.md"),
                @RamlDocumentation(title = "Usage", content = "apispecs/documentation/usage.md")
        },
        security = {
                @RamlSecurity(
                        key = "custom-token",
                        type = "x-custom-token",
                        describedBy = @RamlDescriptor(
                                headers = {
                                        @RamlParameter(
                                                name = "Authorization",
                                                description = "the custom authorization token",
                                                type = "string",
                                                example = "custom-token eyJhbGciOiJSUzI1NiIsImN0eSI6ImFwcGxpY2F0aW9uXC9qc29uIn0",
                                                pattern = "custom-token (.+)")
                                },
                                responses = {
                                        @RamlResponse(
                                                status = 401,
                                                description = "invalid or expired token",
                                                bodies = {
                                                        @RamlBody(
                                                                contentType = MediaType.APPLICATION_JSON,
                                                                schema = "apispecs/resources/schemas/errors/standard-error-response.json",
                                                                example = "apispecs/resources/examples/errors/not-authorized-response.json"
                                                        )
                                                }
                                        ),
                                        @RamlResponse(
                                                status = 403,
                                                description = "user is forbidden from accessing the resource",
                                                bodies = {
                                                        @RamlBody(
                                                                contentType = MediaType.APPLICATION_JSON,
                                                                schema = "apispecs/resources/schemas/errors/standard-error-response.json",
                                                                example = "apispecs/resources/examples/errors/forbidden-response.json"
                                                        )}
                                        ),
                                        @RamlResponse(
                                                status = 503,
                                                description = "secure requests are currently unavailable",
                                                bodies = {
                                                        @RamlBody(
                                                                contentType = MediaType.APPLICATION_JSON,
                                                                schema = "apispecs/resources/schemas/errors/standard-error-response.json",
                                                                example = "apispecs/resources/examples/errors/security-unavailable-response.json"
                                                        )}
                                        )}
                        )
                ),
                @RamlSecurity(
                        key = "group-token",
                        type = "x-group-token",
                        describedBy = @RamlDescriptor(
                                queryParameters = @RamlParameter(
                                        name = "token",
                                        description = "one or multiple group tokens",
                                        type = "string",
                                        example = "token-1234",
                                        multiple = true
                                )
                        )
                )
        },
        traits = {
                @RamlTrait(
                        key = "has404",
                        describedBy = @RamlDescriptor(
                                responses = {
                                        @RamlResponse(
                                                status = 404,
                                                description = "resource could not be found",
                                                bodies = {
                                                        @RamlBody(
                                                                contentType = MediaType.APPLICATION_JSON,
                                                                schema = "apispecs/resources/schemas/errors/standard-error-response.json",
                                                                example = "apispecs/resources/examples/errors/not-found-response.json"
                                                        )
                                                }
                                        )
                                }
                        )
                ),
                @RamlTrait(
                        key = "isValidated",
                        describedBy = @RamlDescriptor(
                                responses = {
                                        @RamlResponse(
                                                status = 422,
                                                description = "request failed validation",
                                                bodies = {
                                                        @RamlBody(
                                                                contentType = MediaType.APPLICATION_JSON,
                                                                schema = "apispecs/resources/schemas/errors/validation-error-response.json",
                                                                example = "apispecs/resources/examples/errors/validation-error-response.json"
                                                        )
                                                }
                                        )}
                        )
                )
        }
)
public class TestAppDocs {
}
