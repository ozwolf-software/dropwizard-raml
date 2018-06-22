package net.ozwolf.raml.maven;

import com.google.common.base.Joiner;
import net.ozwolf.raml.generator.RamlGenerator;
import net.ozwolf.raml.generator.exception.RamlGenerationException;
import net.ozwolf.raml.generator.exception.RamlGenerationUnhandledException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.raml.v2.api.RamlModelBuilder;
import org.raml.v2.api.RamlModelResult;
import org.raml.v2.api.model.common.ValidationResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Mojo(name = "generate")
public class RamlGenerateMojo extends AbstractMojo {
    @Parameter(property = "generate.basePackage")
    private String basePackage;

    @Parameter(property = "generate.version")
    private String version;

    @Parameter(property = "generate.outputFile", defaultValue = "${basedir}/target/classes/apidocs/apidocs.raml")
    private String outputFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        boolean missingProperties = false;

        if (StringUtils.isBlank(basePackage)) {
            missingProperties = true;
            getLog().error("a [ basePackage ] must be provided");
        }

        if (StringUtils.isBlank(version)) {
            missingProperties = true;
            getLog().error("a [ version ] must be provided");
        }

        if (StringUtils.isBlank(outputFile)) {
            missingProperties = true;
            getLog().error("an [ outputFile ] must be provided");
        }

        if (missingProperties) return;

        try {
            File output = new File(outputFile);
            if (!output.exists() && !output.getParentFile().mkdirs()) {
                throw new MojoExecutionException("Unable to create output file directory [ " + output.getParentFile().getName() + " ]");
            }

            if (output.exists() && !output.isFile()) {
                throw new MojoExecutionException("[ " + output.getName() + " ] exists but is not a file.");
            }

            CaseUtils.toCamelCase()

            RamlGenerator generator = new RamlGenerator(basePackage, version);
            String raml = generator.generate();

            RamlModelResult validation = new RamlModelBuilder().buildApi(raml, "/");

            if (!validation.getValidationResults().isEmpty()) {
                getLog().error("RAML is not valid.");
                getLog().error(makeError(validation.getValidationResults()));
            } else {
                FileUtils.writeStringToFile(output, raml);
            }
        } catch (RamlGenerationException e) {
            throw new MojoExecutionException("RAML code documentation is invalid.", e);
        } catch (IOException | RamlGenerationUnhandledException e) {
            throw new MojoExecutionException("An unexpected error was encountered", e);
        }
    }

    private String makeError(List<ValidationResult> errors) {
        List<String> lines = newArrayList("RAML validation failed for the following reasons:");
        errors.forEach(r -> lines.add(r.getMessage()));
        return Joiner.on("\n").join(lines);
    }
}
