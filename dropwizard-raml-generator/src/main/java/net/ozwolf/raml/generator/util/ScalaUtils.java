package net.ozwolf.raml.generator.util;

import scala.Predef;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;

import java.util.HashMap;
import java.util.Map;

public class ScalaUtils {
    public static <K, V> Map<K, V> convertToJava(scala.collection.immutable.Map<K, V> map) {
        return new HashMap<>(JavaConversions.mapAsJavaMap(map));
    }

    public static <K, V> scala.collection.immutable.Map<K, V> convertToScala(Map<K, V> map) {
        return JavaConverters.mapAsScalaMapConverter(map).asScala().toMap(Predef.conforms());
    }
}
