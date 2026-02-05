package lol.pbu

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Shared
import spock.lang.Specification

import java.util.regex.Pattern

@MicronautTest
class ZcmiSpec extends Specification {

    @Shared
    ZcmiTestContext testContext

    @Shared
    Pattern blank, success

    @Shared
    def gradleProperties

    def setupSpec() {
        testContext = new ZcmiTestContext(
                createContext(),
                createContext(["micronaut.http.services.zendesk.token": "this-is-an-invalid-token"]),
                createContext(["micronaut.http.services.zendesk.email": "this-is-an-invalid-email"]),
                createContext(["micronaut.http.services.zendesk.url": "https://fake-url.lol"])
        )
        gradleProperties = new Properties()
        new File('gradle.properties').withInputStream { stream ->
            gradleProperties.load(stream)
        }
        String ansi = "\\u001B\\[[;\\d]*m"
        blank = Pattern.compile "^\\s*\$"
        success = Pattern.compile("^${ansi}\\w+${ansi}\\s*\$")
    }

    def cleanupSpec() {
        testContext.close()
    }

    /**
     * Executes a command with the given arguments and a specific application context,
     * capturing stdout and stderr.
     *
     * @param ctx The application context to use for running the command.
     * @param args The arguments to pass to the command.
     * @return An array containing the captured stdout (at index 0) and stderr (at index 1)
     *         as String representations of the output streams.
     */
    String[] executeCommand(ApplicationContext ctx, String... args) {
        OutputStream out = new ByteArrayOutputStream()
        OutputStream err = new ByteArrayOutputStream()
        System.setOut(new PrintStream(out))
        System.setErr(new PrintStream(err))
        PicocliRunner.run(ZcmiCommand.class, ctx, args)
        return new String[]{out.toString(), err.toString()}
    }

    /**
     * create an ApplicationContext for zcmi, assigning the zendesk properties needed to use the z4j library
     *
     * @param authUser environment variable whose value holds the user's email address
     * @return ApplicationContext with the specified environment variables
     */
    static ApplicationContext createContext(Map<String, String> overridingProperties = [:]) {
        def properties = ["micronaut.http.services.zendesk.email": System.getenv("ZCMI_ADMIN_EMAIL"),
                          "micronaut.http.services.zendesk.url"  : System.getenv("ZCMI_SANDBOX_URL"),
                          "micronaut.http.services.zendesk.token": System.getenv("ZCMI_ADMIN_TOKEN")] + overridingProperties
        ApplicationContext.builder(EmbeddedServer).properties(properties).build().start()
    }
}
