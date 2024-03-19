package sh.miles.cosmictools.utils

/**
 * The available sources to download CosmicReach from
 */
enum class DownloadSource(official: Boolean) {
    UNOFFICIAL_GITHUB(false),
    SELENIUM(true),
}
