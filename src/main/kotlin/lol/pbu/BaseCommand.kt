package lol.pbu

import io.micronaut.core.annotation.ReflectiveAccess
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Help.Ansi.AUTO
import picocli.CommandLine.Model.CommandSpec
import picocli.CommandLine.Spec
import java.io.PrintWriter

@Command
open class BaseCommand {
    @Spec
    @ReflectiveAccess
    protected var spec: CommandSpec? = null


    fun out(message: String) {
        outWriter()?.println(AUTO.string(message))
    }

    fun err(message: String?) {
        errWriter()?.println(AUTO.string("@|bold,red | Error|@ $message"))
    }

    fun warning(message: String?) {
        outWriter()?.println(AUTO.string("@|bold,red | Warning|@ $message"))
    }

    fun green(message: String?) {
        outWriter()?.println(AUTO.string("@|bold,green $message|@"))
    }

    fun red(message: String?) {
        outWriter()?.println(AUTO.string("@|bold,red $message|@"))
    }

    fun outWriter(): PrintWriter? = spec?.commandLine()?.getOut()

    fun errWriter(): PrintWriter? = spec?.commandLine()?.getErr()

}