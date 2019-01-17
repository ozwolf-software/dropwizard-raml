package net.ozwolf.raml.common.model;

/**
 * <h1>RAML Body</h1>
 *
 * A body specification
 */
public interface RamlBody {
    /**
     * The content type the body represents
     *
     * @return the content type
     */
    String getContentType();

    /**
     * The schema (if any) for the body
     *
     * @return the schema
     */
    String getSchema();

    /**
     * The example (if any) for the body
     *
     * @return the example
     */
    String getExample();
}
