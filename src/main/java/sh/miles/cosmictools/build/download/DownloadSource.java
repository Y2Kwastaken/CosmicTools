package sh.miles.cosmictools.build.download;

import sh.miles.cosmictools.build.BuildData;

/**
 * A Download source for the source code of Cosmic Reach
 */
public interface DownloadSource {
    /**
     * Downloads CosmicReach and returns the given BuildData
     *
     * @return the build data
     */
    BuildData download(boolean download);

    static DownloadSource newPreferredSource() {
        return new ItchSource();
    }
}
