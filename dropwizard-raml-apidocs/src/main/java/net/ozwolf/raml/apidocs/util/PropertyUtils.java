package net.ozwolf.raml.apidocs.util;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <h1>Version Utilities</h1>
 *
 * A small utility class for retrieving version numbers.
 */
public class PropertyUtils {
    /**
     * Retrieve a property value from a given property resource file
     *
     * @param resource     the properties resource file
     * @param propertyName the property name
     * @return the property value
     * @throws IllegalArgumentException if there was an error opening the resource stream
     * @throws IllegalArgumentException if the properties resource or property key could not be found
     */
    public static String getValueFrom(String resource, String propertyName) {
        try (InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null)
                throw new IllegalArgumentException("Properties resource [ " + resource + " ] could not be found.");

            Properties p = new Properties();
            p.load(in);

            String version = StringUtils.trimToNull(p.getProperty(propertyName));
            if (version == null)
                throw new IllegalArgumentException("Could not find version property [ " + propertyName + " ] with value.");

            return version;
        } catch (IOException e) {
            throw new IllegalStateException("Could not load properties resource.", e);
        }
    }
}
