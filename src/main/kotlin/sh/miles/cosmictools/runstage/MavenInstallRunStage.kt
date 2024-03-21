package sh.miles.cosmictools.runstage

import joptsimple.OptionSet
import sh.miles.cosmictools.COSMIC_REACH_DOWNLOAD
import sh.miles.cosmictools.IS_WINDOWS
import sh.miles.cosmictools.NO_INSTALL
import sh.miles.cosmictools.SUCCESS
import sh.miles.cosmictools.download.VersionData
import sh.miles.cosmictools.utils.CurrentDirectory
import java.nio.file.Path
import java.util.LinkedList

class MavenInstallRunStage : RunStage {

    override fun runStage(options: OptionSet, propagate: MutableMap<String, Any>): Int {
        if (options.has(NO_INSTALL)) {
            return SUCCESS
        }

        val bin = propagate["maven-bin"] as Path
        val versionData = propagate["version_data"] as VersionData
        val parameters = genericParameters(bin, versionData)
        if (!IS_WINDOWS) {
            parameters.addFirst("sh")
        }

        ProcessBuilder().command(parameters).inheritIO().start().waitFor()
        return SUCCESS
    }

    private fun genericParameters(bin: Path, versionData: VersionData): LinkedList<String> {
        val deque = LinkedList<String>()
        deque.addLast(bin.toAbsolutePath().toString())
        deque.addLast("install:install-file")
        deque.addLast(
            "-Dfile=${
                COSMIC_REACH_DOWNLOAD.resolve(versionData.fileVersion)
                    .resolve("Cosmic Reach-${versionData.version}.jar")
            }"
        )
        deque.addLast("-DgroupId=finalforeach")
        deque.addLast("-DartifactId=cosmicreach")
        deque.addLast("-Dversion=${versionData.version}")
        deque.addLast("-Dpackaging=jar")
        deque.addLast("-DgeneratePom=true")
        return deque
    }

}
