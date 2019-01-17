package net.ozwolf.raml.validator;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NodeTest {
    @Test
    public void shouldReturnProperlyNotatedPath() {
        Node parent = new Node("parent");
        Node child = new Node(parent, "child");
        Node grandChild = new Node(child, "grandChild");
        Node grandChildIndex = new Node(grandChild, 1);

        assertThat(grandChild.toString()).isEqualTo("parent.child.grandChild");
        assertThat(grandChildIndex.toString()).isEqualTo("parent.child.grandChild.[1]");
    }

    @Test
    public void shouldProperlyEqual(){
        Node parent = new Node("parent");
        Node child1 = new Node(parent, "child");
        Node child2 = new Node(parent, "child");
        Node grandChild = new Node(child1, "grandChild");

        assertThat(child1.equals(child2)).isTrue();
        assertThat(child1.equals(grandChild)).isFalse();
    }
}