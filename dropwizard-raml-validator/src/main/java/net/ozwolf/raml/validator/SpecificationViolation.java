package net.ozwolf.raml.validator;

public class SpecificationViolation {
    private final Node node;
    private String message;

    public SpecificationViolation(Node node, String message) {
        this.node = node;
        this.message = message;
    }

    public Node getNode() {
        return node;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "[" + this.getClass().getSimpleName() + "<path=" + node.toString() + ", message=" + message + ">]";
    }
}
