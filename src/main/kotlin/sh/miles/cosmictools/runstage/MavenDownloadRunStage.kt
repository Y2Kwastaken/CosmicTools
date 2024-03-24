package sh.miles.cosmictools.runstage

import joptsimple.OptionSet
import sh.miles.cosmictools.IGNORE_CACHING
import sh.miles.cosmictools.IS_WINDOWS
import sh.miles.cosmictools.SUCCESS
import sh.miles.cosmictools.downloadFile
import sh.miles.cosmictools.unzip
import sh.miles.cosmictools.utils.CurrentDirectory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class MavenDownloadRunStage : RunStage {

    companion object {
        private const val MAVEN_VERSION: String = "3.9.6"
        private const val MAVEN_HASH: String =
            "0eb0432004a91ebf399314ad33e5aaffec3d3b29279f2f143b2f43ade26f4db7bd1c0f08e436e9445ac6dc4a564a2945d13072a160ae54a930e90581284d6461"
        val MAVEN_FOLDER: String = "apache-maven-$MAVEN_VERSION"
        val MAVEN_FILE: String = "$MAVEN_FOLDER-bin.zip"
        val MAVEN_DOWNLOAD: String = "https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/$MAVEN_FILE"
    }


    override fun runStage(options: OptionSet, propagate: MutableMap<String, Any>): Int {
        val maven = CurrentDirectory.cwd().resolve(MAVEN_FOLDER)
        propagateBin(maven, propagate)
        if (Files.exists(maven) && !options.has(IGNORE_CACHING)) return SUCCESS
        val m2 = System.getenv("M2_HOME")
        if (m2 == null || !Files.exists(Path.of(m2))) {
            if (Files.notExists(maven)) {
                downloadMaven()
            }
        }

        return SUCCESS
    }

    @Throws(IOException::class)
    fun downloadMaven() {
        println("Maven is not downloaded, downloading maven.")
        val tmp = CurrentDirectory.cwd().resolve(MAVEN_FILE)
        downloadFile(MAVEN_DOWNLOAD, tmp)
        unzip(
            tmp, CurrentDirectory.cwd()
        ) { true }
        Files.deleteIfExists(tmp)
        println("Finished downloading maven, maven is now downloaded.")
    }

    private fun propagateBin(maven: Path, propagate: MutableMap<String, Any>) {
        if (IS_WINDOWS) {
            propagate["maven-bin"] = maven.resolve("/bin/mvn.cmd")
        } else {
            propagate["maven-bin"] = maven.resolve("/bin/mvn")
        }
    }
}
