package lol.pbu

import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest

@MicronautTest
class ZcmiCommandSpec extends ZcmiSpec {

    void "querying version works no matter the context"(String[] args, ApplicationContext context) {
        when:
        def (out, err) = executeCommand(context, args)

        then:
        verifyAll {
            out.trim() == gradleProperties.getProperty("zcmiVersion")
            err.matches(blank)
        }

        where:
        [args, context] << [[['-V'], ['--version']], testContext.getAllContexts()].combinations()
    }

    void "invoking with no arguments prints error"(ApplicationContext context) {
        when:
        String[] out = executeCommand(context)

        then:
        verifyAll {
            out.first().contains("No Command Given")
        }

        where:
        context << testContext.getAllContexts()
    }
}
