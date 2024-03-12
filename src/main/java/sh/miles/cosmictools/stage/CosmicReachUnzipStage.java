package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.Utils;
import sh.miles.cosmictools.util.CosmicReachInfo;
import sh.miles.cosmictools.util.NeoConstants;

import java.util.Map;

public class CosmicReachUnzipStage implements RunStage {
    @Override
    public int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception {
        final CosmicReachInfo info = (CosmicReachInfo) propagate.getOrDefault("cosmic-reach-info", null);
        if (info == null) {
            System.out.println("No CosmicReachInfo is present this issue propagated from CosmicReachDownloadStage");
            return NeoConstants.FAILURE;
        }

        Utils.unzip(NeoConstants.COSMIC_REACH_ZIP_PATH, NeoConstants.COSMIC_REACH_DOWNLOAD.resolve(info.fileVersionName()), (l) -> true);
        return NeoConstants.SUCCESS;
    }
}
