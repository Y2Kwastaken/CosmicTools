package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.Utils;
import sh.miles.cosmictools.NeoFlags;
import sh.miles.cosmictools.util.CosmicReachInfo;
import sh.miles.cosmictools.util.NeoConstants;

import java.nio.file.Path;
import java.util.Map;

public class CosmicReachDecompileStage implements RunStage {

    public static final Path DECOMPILE_PATH = NeoConstants.COSMIC_REACH.resolve("decompile");
    public static final String COSMIC_REACH_JAR = "Cosmic Reach-%s.jar";

    @Override
    public int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception {
        if (!options.has(NeoFlags.DECOMPILE)) {
            return NeoConstants.SUCCESS;
        }

        final CosmicReachInfo info = (CosmicReachInfo) propagate.getOrDefault("cosmic-reach-info", null);

        if (info == null) {
            System.out.println("The cosmic-reach-info property is not found");
            return NeoConstants.FAILURE;
        }

        final Path jarLocation = NeoConstants.COSMIC_REACH_DOWNLOAD.resolve(info.fileVersionName()).resolve(COSMIC_REACH_JAR.formatted(info.version()));
        Utils.unzip(jarLocation, DECOMPILE_PATH.resolve("classes"), (s) -> true);

        System.out.println("Starting decompilation of Cosmic Reach");
        new ProcessBuilder()
                .command("java", "-jar", "vineflower.jar", "-dgs=1", "-hdc=0", "-rbr=0", "-asc=1", "-udc=0",
                        DECOMPILE_PATH.resolve("classes").toString(), DECOMPILE_PATH.resolve("java").toString())
                .inheritIO()
                .start()
                .waitFor();
        System.out.println("Finished decompiling Cosmic Reach");
        return NeoConstants.SUCCESS;
    }
}
