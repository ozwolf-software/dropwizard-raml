package net.ozwolf.raml.generator.util;

import com.google.common.io.Resources;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class ClassPathUtils {
    private final static Map<String, String> LOADED = newHashMap();

    public static String getResourceAsString(String resource) {
        try {
            if (LOADED.containsKey(resource.toLowerCase())) return LOADED.get(resource.toLowerCase());

            if (StringUtils.isBlank(resource))
                return null;

            URL url = ClassPathUtils.class.getClassLoader().getResource(resource);
            if (url == null)
                return resource;

            String content = Resources.toString(url, Charset.defaultCharset());
            if (StringUtils.isNotBlank(content))
                LOADED.put(resource.toLowerCase(), content);

            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error trying to load resource [ " + resource + " ].", e);
        }
    }
}
