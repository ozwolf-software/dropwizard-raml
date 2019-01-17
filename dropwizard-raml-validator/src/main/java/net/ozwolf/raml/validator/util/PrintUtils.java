package net.ozwolf.raml.validator.util;

import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class PrintUtils {
    public static String prettyPrint(List<SpecificationViolation> violations) {
        List<String> lines = getLines(toMap(violations), 0);
        return StringUtils.join(lines, "\n");
    }

    @SuppressWarnings("unchecked")
    private static List<String> getLines(Map<String, Object> node, int indent) {
        List<String> lines = newArrayList();
        String tabs = StringUtils.repeat("\t", indent);
        for (Map.Entry<String, Object> entry : node.entrySet()) {
            if (entry.getValue() instanceof Map) {
                lines.add(tabs + "[ " + entry.getKey() + " ]");
                lines.addAll(getLines((Map<String, Object>) entry.getValue(), indent + 1));
            } else {
                lines.add(tabs + "[ " + entry.getKey() + " ] - " + entry.getValue());
            }
        }
        return lines;
    }

    private static Map<String, Object> toMap(List<SpecificationViolation> violations) {
        Map<String, Object> result = newHashMap();
        for (SpecificationViolation violation : violations) result = deepMerge(result, toBranch(violation));
        return result;
    }

    private static Map<String, Object> toBranch(SpecificationViolation violation) {
        Map<String, Object> branch = newHashMap();
        Node node = violation.getNode();
        branch.put(node.getName(), violation.getMessage());

        Node parent = node.getParent();
        while (parent != null) {
            Map<String, Object> parentBranch = newHashMap();
            parentBranch.put(parent.getName(), branch);
            branch = parentBranch;
            parent = parent.getParent();
        }

        return branch;
    }

    private static Map<String, Object> deepMerge(Map<String, Object> original, Map<String, Object> delta) {
        for (String key : delta.keySet()) {
            Object newValue = delta.get(key);
            if (newValue instanceof Map) {
                Map<String, Object> existingBranch = (Map<String, Object>) original.computeIfAbsent(key, k -> newHashMap());
                Map<String, Object> newBranch = (Map<String, Object>) newValue;
                original.put(key, deepMerge(existingBranch, newBranch));
            } else {
                original.put(key, newValue);
            }
        }
        return original;
    }
}
