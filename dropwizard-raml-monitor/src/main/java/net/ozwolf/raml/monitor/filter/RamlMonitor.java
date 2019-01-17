package net.ozwolf.raml.monitor.filter;

import net.ozwolf.raml.monitor.ApiMonitorBundle;
import net.ozwolf.raml.validator.*;
import net.ozwolf.raml.validator.exception.SpecificationViolationException;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class RamlMonitor {
    private final ExecutorService executor;

    public RamlMonitor(ExecutorService executor) {
        this.executor = executor;
    }

    public void validate(ApiRequest request, ApiResponse response) {
        executor.execute(() -> {
            String nodeName = request.getMethod().toUpperCase() + " " + request.getPath();
            nodeName += request.getMediaType() == null ? "" : " (" + request.getMediaType() + ")";
            nodeName += " " + response.getStatus();

            Node node = new Node(nodeName);

            List<SpecificationViolation> violations = new ApiValidator().validate(node, request, response);
            if (!violations.isEmpty())
                ApiMonitorBundle.LOGGER.warn("Request generated specification violations:\n" + new SpecificationViolationException(violations).getPrintedViolationsMessage() + "\n");
        });
    }
}
