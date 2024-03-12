package sh.miles.cosmictools;

import joptsimple.OptionSet;
import sh.miles.cosmictools.stage.CosmicReachDecompileStage;
import sh.miles.cosmictools.stage.CosmicReachDownloadStage;
import sh.miles.cosmictools.stage.CosmicReachUnzipStage;
import sh.miles.cosmictools.stage.MavenDownloadStage;
import sh.miles.cosmictools.stage.MavenInstallStage;
import sh.miles.cosmictools.stage.RunStage;
import sh.miles.cosmictools.stage.VineFlowerDownloadStage;
import sh.miles.cosmictools.util.NeoConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The main class for CosmicTools
 */
public final class CosmicTools {

    private static final LinkedList<RunStage> stages = new LinkedList<>();

    static {
        stages.add(new MavenDownloadStage());
        stages.add(new CosmicReachDownloadStage());
        stages.add(new CosmicReachUnzipStage());
        stages.add(new VineFlowerDownloadStage());
        stages.add(new CosmicReachDecompileStage());
        stages.add(new MavenInstallStage());
    }

    /**
     * The entry point of CosmicTools
     *
     * @param args the main arguments
     */
    public static void main(String[] args) {
        final long start = System.currentTimeMillis();
        final OptionSet options = NeoFlags.PARSER.parse(args);

        if (options.has(NeoFlags.HELP)) {
            try {
                NeoFlags.PARSER.printHelpOn(System.out);
            } catch (IOException ignore) {
            }
            System.exit(NeoConstants.SUCCESS);
        }

        final Map<String, Object> propagationMap = new HashMap<>();
        int exitCode = NeoConstants.SUCCESS;
        try {
            for (final RunStage stage : stages) {
                exitCode = stage.runStage(options, propagationMap);
                if (exitCode == NeoConstants.FAILURE) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(NeoConstants.FAILURE);
        }
        final long end = System.currentTimeMillis();
        System.out.printf("Finished Running CosmicTools in %d seconds%n", (end - start) / 1000);

        System.exit(exitCode);
    }

}
