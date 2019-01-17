package net.ozwolf.raml.monitor;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.ozwolf.raml.common.AbstractRamlBundle;
import net.ozwolf.raml.generator.RamlSpecification;
import net.ozwolf.raml.monitor.filter.RamlMonitorFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * <h1>API Monitor Bundle</h1>
 *
 * This DropWizard bundle is designed to monitor requests being made to the application, compare it with the specification for the service and log any discrepencies between the two.
 */
public class ApiMonitorBundle extends AbstractRamlBundle {
    private Integer monitoringThreads;
    private Integer queueSize;

    private final static Integer DEFAULT_MONITORING_THREADS = 2;
    private final static Integer DEFAULT_MONITORING_QUEUE_SIZE = 200;

    public final static Logger LOGGER = LoggerFactory.getLogger("raml-monitor");
    public final static String SCOPE_HEADER = "x-raml-monitor-scope";

    /**
     * @inheritDocs
     */
    public ApiMonitorBundle(RamlSpecification specification) {
        super(specification);
        this.monitoringThreads = DEFAULT_MONITORING_THREADS;
        this.queueSize = DEFAULT_MONITORING_QUEUE_SIZE;
    }

    /**
     * @inheritDocs
     */
    public ApiMonitorBundle(String basePackage, String version) {
        super(basePackage, version);
        this.monitoringThreads = DEFAULT_MONITORING_THREADS;
        this.queueSize = DEFAULT_MONITORING_QUEUE_SIZE;
    }

    /**
     * @inheritDocs
     */
    public ApiMonitorBundle(String ramlFile) {
        super(ramlFile);
        this.monitoringThreads = DEFAULT_MONITORING_THREADS;
        this.queueSize = DEFAULT_MONITORING_QUEUE_SIZE;
    }

    public ApiMonitorBundle withMonitoringCapacity(Integer threads, Integer queueSize) {
        this.monitoringThreads = threads;
        this.queueSize = queueSize;
        return this;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    protected void postInitialization(Environment environment) {
        ExecutorService executor = environment.lifecycle()
                .executorService("raml-monitoring-%d")
                .minThreads(monitoringThreads)
                .maxThreads(monitoringThreads)
                .workQueue(new ArrayBlockingQueue<>(queueSize))
                .rejectedExecutionHandler((r, e) -> LOGGER.warn("RAML monitoring queue size exceeded at [ " + queueSize + " ].  Request will not be monitored."))
                .build();

        environment.jersey().register(new RamlMonitorFeature(getSpecification(), executor));
    }
}
