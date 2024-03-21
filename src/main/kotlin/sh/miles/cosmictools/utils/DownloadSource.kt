package sh.miles.cosmictools.utils

import sh.miles.cosmictools.download.CosmicReachSource
import sh.miles.cosmictools.download.ItchSeleniumSource
import sh.miles.cosmictools.download.UnofficialGithubSource

/**
 * The available sources to download CosmicReach from
 */
enum class DownloadSource(val official: Boolean, val sourceBuilder: () -> CosmicReachSource) {
    SELENIUM(true, { ItchSeleniumSource }),
    UNOFFICIAL_ARCHIVE_GITHUB(false, { UnofficialGithubSource }),
}
