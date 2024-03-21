package sh.miles.cosmictools.download

import java.nio.file.Path

/**
 * Represents a Source that can be used to download CosmicReach
 */
interface CosmicReachSource : AutoCloseable {
    /**
     * Downloads cosmic reach from the version data
     *
     * @param version the version to download
     * @param destination the destination of the downloaded jar
     */
    fun download(version: VersionData, destination: Path)

    /**
     * Uses Web Requests or other tactics to gain as much version data as possible on Cosmic Reach
     *
     * @return a list of versions
     */
    fun info(): List<VersionData>

    /**
     * Initializes a cosmic reach source with the needed parameters
     *
     * @param parameters
     */
    fun initialize(vararg parameters: Any): CosmicReachSource
}
