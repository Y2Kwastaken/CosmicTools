package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.NeoFlags;
import sh.miles.cosmictools.download.CosmicReachSource;
import sh.miles.cosmictools.util.CosmicReachInfo;
import sh.miles.cosmictools.utils.DriverType;
import sh.miles.cosmictools.util.NeoConstants;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class CosmicReachDownloadStage implements RunStage {

    @Override
    public int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception {
        final DriverType driver = NeoFlags.DRIVER.value(options);
        if (driver == null) {
            System.out.println("No driver type is present");
            return NeoConstants.FAILURE;
        }

        try (CosmicReachSource source = CosmicReachSource.getItchSource(driver)) {
            final CosmicReachInfo info = source.retrieveBuildData();
            propagate.put("cosmic-reach-info", info);

            if (Files.notExists(NeoConstants.COSMIC_REACH_DOWNLOAD.resolve(info.fileVersionName())) || options.has(NeoFlags.IGNORE_CACHE)) {
                System.out.println("Downloading CosmicReach");
                source.download(info);
                System.out.printf("Downloaded CosmicReach, Moving from %s to %s%n", NeoConstants.USER_DOWNLOAD_COSMIC_REACH, NeoConstants.COSMIC_REACH_ZIP_PATH);
                Files.move(NeoConstants.USER_DOWNLOAD_COSMIC_REACH, NeoConstants.COSMIC_REACH_ZIP_PATH, StandardCopyOption.REPLACE_EXISTING);

            }
        }

        return NeoConstants.SUCCESS;
    }

}
