package net.ozwolf.raml.validator.media;

import net.ozwolf.raml.validator.Node;
import net.ozwolf.raml.validator.SpecificationViolation;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.ByteArrayInputStream;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class XmlMediaValidator implements MediaValidator {
    private XmlMediaValidator() {
    }

    @Override
    public List<SpecificationViolation> validate(Node node, String schema, byte[] content) {
        List<SpecificationViolation> violations = newArrayList();
        if (StringUtils.trimToNull(schema) == null)
            return violations;

        ReportErrorHandler handler = new ReportErrorHandler(node);

        try {
            Schema xsd = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(new ByteArrayInputStream(schema.getBytes())));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);
            factory.setSchema(xsd);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(handler);
            builder.parse(new ByteArrayInputStream(content));
            violations.addAll(handler.violations);
        } catch (Exception e) {
            violations.add(new SpecificationViolation(node.createChild("content"), "XML document did not pass validation (" + e.getMessage() + ")"));
        }

        return violations;
    }

    public static XmlMediaValidator newInstance() {
        return new XmlMediaValidator();
    }

    private static class ReportErrorHandler implements ErrorHandler {
        private final Node node;
        private final List<SpecificationViolation> violations;

        ReportErrorHandler(Node node) {
            this.node = node;
            this.violations = newArrayList();
        }

        @Override
        public void warning(SAXParseException exception) {
            // DO NOTHING
        }

        @Override
        public void error(SAXParseException exception) {
            recordViolation(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) {
            recordViolation(exception);
        }

        private void recordViolation(SAXParseException exception) {
            Node errorNode = new Node(node, "line-" + exception.getLineNumber());
            if (StringUtils.containsIgnoreCase(exception.getMessage(), "Document"))
                return;

            Long nextIndex = violations.stream().filter(v -> v.getNode().getParent().equals(errorNode)).count();

            Node actualNode = new Node(errorNode, nextIndex.intValue());

            violations.add(new SpecificationViolation(actualNode, exception.getMessage()));
        }
    }
}
