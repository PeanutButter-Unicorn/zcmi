package lol.pbu;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.io.PrintWriter;
import java.util.Optional;

import static picocli.CommandLine.Help.Ansi.AUTO;

@Command
public class BaseCommand {

    @Spec
    @ReflectiveAccess
    protected CommandSpec spec;

    public void err(String message) {
        errWriter().ifPresent(writer -> writer.println(AUTO.string("@|bold,red " + message + "|@")));
    }

    @NonNull
    public Optional<PrintWriter> errWriter() {
        return getSpec().map(commandSpec -> commandSpec.commandLine().getErr());
    }

    @NonNull
    public Optional<CommandSpec> getSpec() {
        return Optional.ofNullable(spec);
    }
}
