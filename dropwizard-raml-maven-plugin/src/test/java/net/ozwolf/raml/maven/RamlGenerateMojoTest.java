package net.ozwolf.raml.maven;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class RamlGenerateMojoTest extends AbstractMojoTestCase {
    private final static String TEST_POM = "target/test-classes/test-pom.xml";

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void shouldInstantiatePlugin() throws Exception {
        File pom = new File(getBasedir(), TEST_POM);

        RamlGenerateMojo result = (RamlGenerateMojo) lookupMojo("generate", pom);
        assertThat(result).isNotNull();
    }
}