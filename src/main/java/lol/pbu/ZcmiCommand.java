package lol.pbu;

import io.micronaut.configuration.picocli.PicocliRunner;

import io.micronaut.context.annotation.Value;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.jansi.graalvm.AnsiConsole;

@Command(name = "zcmi", description = "...", versionProvider = ZcmiCommand.ZcmiVersionProvider.class, mixinStandardHelpOptions = true)
public class ZcmiCommand extends BaseCommand implements Runnable {

    @Generated
    public static void main(String[] args) {
        try (AnsiConsole ignored = AnsiConsole.windowsInstall()) {
            PicocliRunner.run(ZcmiCommand.class, args);
        }
    }

    public void run() {
        err("No Command Given");
    }

    static class ZcmiVersionProvider implements CommandLine.IVersionProvider {
        @Value("${micronaut.application.version}")
        String version;

        @Override
        public String[] getVersion() {
            return new String[]{version};
        }
    }
}
