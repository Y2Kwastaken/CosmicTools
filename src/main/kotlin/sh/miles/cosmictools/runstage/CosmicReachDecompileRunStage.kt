package sh.miles.cosmictools.runstage

import joptsimple.OptionSet
import sh.miles.cosmictools.COSMIC_REACH
import sh.miles.cosmictools.COSMIC_REACH_DECOMPILE
import sh.miles.cosmictools.COSMIC_REACH_DOWNLOAD
import sh.miles.cosmictools.DECOMPILE
import sh.miles.cosmictools.IGNORE_CACHING
import sh.miles.cosmictools.SUCCESS
import sh.miles.cosmictools.VINE_FLOWER
import sh.miles.cosmictools.download.VersionData
import sh.miles.cosmictools.downloadFile
import sh.miles.cosmictools.unzip
import sh.miles.cosmictools.utils.CurrentDirectory
import java.nio.file.Files
import java.nio.file.Path

class CosmicReachDecompileRunStage : RunStage {

    companion object {
        const val VINE_FLOWER_LINK: String =
            "https://github.com/Vineflower/vineflower/releases/download/1.9.3/vineflower-1.9.3.jar"
    }


    override fun runStage(options: OptionSet, propagate: MutableMap<String, Any>): Int {
        if (!options.has(DECOMPILE)) return SUCCESS

        if (Files.notExists(VINE_FLOWER) || options.has(IGNORE_CACHING)) {
            println("Downloading Vineflower to $VINE_FLOWER")
            downloadFile(VINE_FLOWER_LINK, VINE_FLOWER)
            println("Finished downloading Vineflower at $VINE_FLOWER")
        }


        val data = propagate["version_data"] as VersionData
        val jarLocation = COSMIC_REACH_DOWNLOAD.resolve(data.fileVersion).resolve("Cosmic Reach-${data.version}.jar")
        val javaDest = COSMIC_REACH_DECOMPILE.resolve(data.fileVersion).resolve("java")
        val classDest = COSMIC_REACH_DECOMPILE.resolve(data.fileVersion).resolve("classes")

        if (Files.notExists(classDest) || options.has(IGNORE_CACHING)) {
            unzipJar(jarLocation, classDest)
        }

        if (Files.notExists(javaDest) || options.has(IGNORE_CACHING)) {
            decompileJar(classDest, javaDest)
        }

        return SUCCESS
    }

    private fun unzipJar(jarLocation: Path, classDest: Path) {
        unzip(jarLocation, classDest) { true }
    }

    private fun decompileJar(classDest: Path, javaDest: Path) {
        println("Starting de-compilation of Cosmic Reach")
        ProcessBuilder().command(
            "java",
            "-jar",
            CurrentDirectory.cwd().resolve("vineflower.jar").toString(),
            "-dgs=1",
            "-hdc=0",
            "-rbr=0",
            "-asc=1",
            "-udc=0",
            classDest.toString(),
            javaDest.toString()
        ).inheritIO().start().waitFor()
        println("Finished decompiling Cosmic Reach")
    }

}
