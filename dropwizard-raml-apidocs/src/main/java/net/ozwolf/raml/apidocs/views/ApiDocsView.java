package net.ozwolf.raml.apidocs.views;

import io.dropwizard.views.View;
import net.ozwolf.raml.common.model.RamlApplication;

public class ApiDocsView extends View {
    private final RamlApplication application;

    public ApiDocsView(RamlApplication application) {
        super("../../../../../templates/index.ftl");
        this.application = application;
    }

    public RamlApplication getApplication() {
        return application;
    }
}
