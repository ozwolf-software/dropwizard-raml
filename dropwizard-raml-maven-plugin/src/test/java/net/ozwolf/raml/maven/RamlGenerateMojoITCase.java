package net.ozwolf.raml.maven;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;

import static net.ozwolf.raml.maven.matches.RamlMatchers.validRaml;
import static org.assertj.core.api.Assertions.assertThat;

public class RamlGenerateMojoITCase {
    @Rule
    public final MojoRule mojo = new MojoRule();
    @Rule
    public final TestResources resources = new TestResources();
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldInstantiatePluginAndGenerateDocs() throws Exception {
        File base = resources.getBasedir("example-app");
        File pom = new File(base, "pom.xml");

        PlexusConfiguration configuration = mojo.extractPluginConfiguration("dropwizard-raml-maven-plugin", pom);
        RamlGenerateMojo result = (RamlGenerateMojo) mojo.configureMojo(new RamlGenerateMojo(), configuration);
        assertThat(result).isNotNull();

        result.execute();

        File ramlFile = new File("target/test-result/apidocs/apidocs.raml");

        assertThat(ramlFile).exists();

        String raml = FileUtils.readFileToString(ramlFile);

        assertThat(raml).is(validRaml());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenPropertiesAreNotSet() throws Exception {
        File base = resources.getBasedir("invalid-app");
        File pom = new File(base, "pom.xml");

        PlexusConfiguration configuration = mojo.extractPluginConfiguration("dropwizard-raml-maven-plugin", pom);
        RamlGenerateMojo result = (RamlGenerateMojo) mojo.configureMojo(new RamlGenerateMojo(), configuration);
        assertThat(result).isNotNull();

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("one or more required properties not provided");
        result.execute();
    }
}