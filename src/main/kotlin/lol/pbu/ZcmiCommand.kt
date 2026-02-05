package lol.pbu

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.annotation.Value
import lol.pbu.ZcmiCommand.ZcmiVersionProvider
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.jansi.graalvm.AnsiConsole

@Command(
    subcommands = [HelpCenterCommand::class],
    mixinStandardHelpOptions = true,
    name = "Zendesk CLI Management Interface",
    versionProvider = ZcmiVersionProvider::class,
    description = ["Work seamlessly with Zendesk from the command line."]
)
class ZcmiCommand : BaseCommand(), Runnable {
    class ZcmiVersionProvider : CommandLine.IVersionProvider {
        @Value("\${micronaut.application.version}")
        var zcmiVersionProvider: String? = null
        override fun getVersion(): Array<out String?> {
            return arrayOf(zcmiVersionProvider); }
    }

    override fun run() {
        err("No command specified.")
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            AnsiConsole.windowsInstall().use { _ -> PicocliRunner.run(ZcmiCommand::class.java, *args) }
        }
    }
}