package sh.miles.cosmictools.download

import sh.miles.cosmictools.downloadFile
import sh.miles.cosmictools.downloadTextAndFilter
import java.nio.file.Path

object UnofficialGithubSource : CosmicReachSource {

    private const val METADATA_LINK = "https://raw.githubusercontent.com/CRModders/CosmicArchive/main/versions.txt"
    private const val JAR_LINK = "https://github.com/CRModders/CosmicArchive/raw/main/Cosmic%20Reach-%s.jar"

    override fun download(version: VersionData, destination: Path) {
        downloadFile(version.link!!, destination)
    }

    override fun info(): List<VersionData> {
        return downloadTextAndFilter(METADATA_LINK) { true }.map {
            VersionData(it, JAR_LINK.format(it))
        }.toList()
    }

    override fun initialize(vararg parameters: Any): CosmicReachSource {
        return this
    }

    override fun close() {
    }
}
