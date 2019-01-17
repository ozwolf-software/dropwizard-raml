package net.ozwolf.raml.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.google.common.collect.Lists.newLinkedList;

public class Node {
    private final Node parent;
    private final String name;

    public Node(Node parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public Node(Node parent, Integer index) {
        this(parent, "[" + index + "]");
    }

    public Node(String name) {
        this(null, name);
    }

    public Node getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public Node createChild(String node) {
        return new Node(this, node);
    }

    @Override
    public String toString() {
        List<String> parts = newLinkedList();

        parts.add(name);
        Node next = parent;
        while (next != null) {
            parts.add(next.name);
            next = next.parent;
        }

        Collections.reverse(parts);

        return StringUtils.join(parts, ".");
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node))
            return false;

        Node p = (Node) o;

        return this.name.equals(p.name) && Objects.equals(this.parent, p.parent);
    }

    public static Node build(Node parent, String... nodes) {
        Node result = parent;
        for (String node : nodes)
            result = new Node(result, node);

        return result;
    }
}
