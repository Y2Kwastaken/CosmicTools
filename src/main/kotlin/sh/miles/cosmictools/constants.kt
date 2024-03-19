package sh.miles.cosmictools

import sh.miles.cosmictools.util.CurrentDirectory
import java.nio.file.Path
import kotlin.system.exitProcess

const val SUCCESS: Int = 0
const val FAILURE: Int = 1

val USER_DOWNLOAD: Path = Path.of(System.getProperty("user.home"), "Downloads")
val USER_DOWNLOAD_COSMIC_REACH: Path = USER_DOWNLOAD.resolve("cosmic-reach-jar.zip")
val COSMIC_REACH: Path = CurrentDirectory.cwd().resolve("cosmic-reach")
val COSMIC_REACH_DOWNLOAD: Path = COSMIC_REACH.resolve("download")
val COSMIC_REACH_ZIP_PATH: Path = COSMIC_REACH.resolve("cosmic-reach-jar.zip")
val VINE_FLOWER: Path = CurrentDirectory.cwd().resolve("vineflower.jar")


fun andSucceed(function: () -> Unit) {
    function.invoke()
    exitProcess(SUCCESS)
}

fun andFail(function: () -> Unit) {
    function.invoke()
    exitProcess(FAILURE)
}
