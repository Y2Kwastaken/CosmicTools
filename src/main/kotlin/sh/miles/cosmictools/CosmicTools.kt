package sh.miles.cosmictools

import sh.miles.cosmictools.runstage.CosmicReachDecompileRunStage
import sh.miles.cosmictools.runstage.CosmicReachDownloadRunStage
import sh.miles.cosmictools.runstage.MavenDownloadRunStage
import sh.miles.cosmictools.runstage.MavenInstallRunStage
import sh.miles.cosmictools.runstage.RunStage
import sh.miles.cosmictools.utils.CurrentDirectory
import java.nio.file.Path

fun main(args: Array<String>) {
    val options = PARSER.parse(*args)

    if (options.has(HELP)) andSucceed { PARSER.printHelpOn(System.out) }
    if (options.has(CURRENT_WORKING_DIRECTORY)) {
        CurrentDirectory.cwd(Path.of(CURRENT_WORKING_DIRECTORY.value(options)))
    }

    val stages = setupStages()
    val propagate = mutableMapOf<String, Any>()
    for (stage in stages) {
        stage.runStage(options, propagate)
    }
}

private fun setupStages(): List<RunStage> {
    return listOf(
        CosmicReachDownloadRunStage(),
        CosmicReachDecompileRunStage(),
        MavenDownloadRunStage(),
        MavenInstallRunStage()
    )
}
