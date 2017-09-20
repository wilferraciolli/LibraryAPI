package com.library.app.commontests.utils;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Ignore;

import java.io.File;

/**
 * The type Arquillian test utils.
 */
@Ignore
public class ArquillianTestUtils {

    /**
     * Create deployment archive web archive.
     *
     * @return the web archive
     */
    public static WebArchive createDeploymentArchive() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addPackages(true, "com.library.app")
                .addAsResource("persistence-integration.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsWebInfResource("jboss-web.xml")
                .addAsResource("application.properties")
                .setWebXML(new File("src/test/resources/web.xml"))
                .addAsLibraries(
                        Maven.resolver().loadPomFromFile("pom.xml")
                                .resolve("com.google.code.gson:gson", "org.mockito:mockito-core")
                                .withTransitivity().asFile());
    }
}
