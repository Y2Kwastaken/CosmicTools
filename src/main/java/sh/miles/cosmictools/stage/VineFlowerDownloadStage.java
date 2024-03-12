package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.Utils;
import sh.miles.cosmictools.NeoFlags;
import sh.miles.cosmictools.util.NeoConstants;

import java.nio.file.Files;
import java.util.Map;

public class VineFlowerDownloadStage implements RunStage {

    public static final String VINE_FLOWER_LINK = "https://github.com/Vineflower/vineflower/releases/download/1.9.3/vineflower-1.9.3.jar";

    @Override
    public int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception {
        if (!options.has(NeoFlags.DECOMPILE)) {
            return NeoConstants.SUCCESS;
        }

        if (!options.has(NeoFlags.IGNORE_CACHE) && Files.exists(NeoConstants.VINE_FLOWER)) {
            return NeoConstants.SUCCESS;
        }

        System.out.printf("Downloading Vineflower to %s%n", NeoConstants.VINE_FLOWER);
        Utils.download(VINE_FLOWER_LINK, NeoConstants.VINE_FLOWER);
        System.out.printf("Finished downloading Vineflower at %s%n", NeoConstants.VINE_FLOWER);
        return NeoConstants.SUCCESS;
    }
}
