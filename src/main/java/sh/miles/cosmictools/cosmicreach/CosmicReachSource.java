package sh.miles.cosmictools.cosmicreach;

import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.util.CosmicReachInfo;
import sh.miles.cosmictools.util.DriverType;

import java.io.IOException;

public interface CosmicReachSource extends AutoCloseable {
    /**
     * Downloads CosmicReach from the given BuildData
     */
    void download(@NotNull final CosmicReachInfo data) throws IOException;

    /**
     * Retrieves BuildData for CosmicReach
     *
     * @return the cosmic reach build data
     */
    CosmicReachInfo retrieveBuildData();

    static CosmicReachSource getItchSource(DriverType driver) {
        ItchSeleniumSource.INSTANCE.setDriverType(driver);
        return ItchSeleniumSource.INSTANCE;
    }
}
