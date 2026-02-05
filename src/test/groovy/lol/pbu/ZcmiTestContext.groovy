package lol.pbu

import io.micronaut.context.ApplicationContext

class ZcmiTestContext implements AutoCloseable {

    final ApplicationContext adminContext
    final ApplicationContext noAuthContext
    final ApplicationContext badEmailContext
    final ApplicationContext badUrlContext

    ZcmiTestContext(ApplicationContext adminContext,
                    ApplicationContext noAuthContext,
                    ApplicationContext badEmailContext,
                    ApplicationContext badUrlContext) {
        this.adminContext = adminContext
        this.noAuthContext = noAuthContext
        this.badEmailContext = badEmailContext
        this.badUrlContext = badUrlContext
    }

    List<ApplicationContext> getAllContexts() {
        return [adminContext, noAuthContext, badEmailContext, badUrlContext]
    }

    @Override
    void close() {
        getAllContexts().each { it.stop() }
    }
}
