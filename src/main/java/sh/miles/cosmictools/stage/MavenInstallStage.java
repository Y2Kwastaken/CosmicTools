package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.util.CosmicReachInfo;
import sh.miles.cosmictools.util.NeoConstants;

import java.nio.file.Path;
import java.util.Map;

public class MavenInstallStage implements RunStage {

    private static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

    @Override
    public int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception {
        System.out.println("Starting Maven Installation");
        final CosmicReachInfo info = (CosmicReachInfo) propagate.getOrDefault("cosmic-reach-info", null);
        if (info == null) {
            System.out.println("No CosmicReachInfo is present this issue propagated from CosmicReachDownloadStage");
            return NeoConstants.FAILURE;
        }

        final Path mavenBin;
        final String prefix;
        if (IS_WINDOWS) {
            mavenBin = Path.of(MavenDownloadStage.MAVEN_FOLDER).resolve("bin/mvn.cmd");
            prefix = "";
        } else {
            mavenBin = Path.of(MavenDownloadStage.MAVEN_FOLDER).resolve("bin/mvn");
            prefix = "sh";
        }
        new ProcessBuilder()
                .command(
                        prefix, mavenBin.toAbsolutePath().toString(),
                        "install:install-file",
                        "-Dfile=%s".formatted(NeoConstants.COSMIC_REACH_DOWNLOAD.resolve(info.fileVersionName()).resolve(CosmicReachDecompileStage.COSMIC_REACH_JAR.formatted(info.version()))),
                        "-DgroupId=finalforeach",
                        "-DartifactId=cosmicreach",
                        "-Dversion=%s".formatted(info.version()),
                        "-Dpackaging=jar",
                        "-DgeneratePom=true"
                )
                .inheritIO()
                .start()
                .waitFor();
        System.out.println("Finished Maven Installation");

        return NeoConstants.SUCCESS;
    }
}
