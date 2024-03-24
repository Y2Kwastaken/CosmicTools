package sh.miles.cosmictools.runstage

import joptsimple.OptionSet
import sh.miles.cosmictools.COSMIC_REACH_DECOMPILE
import sh.miles.cosmictools.COSMIC_REACH_DOWNLOAD
import sh.miles.cosmictools.EXIT_NOW
import sh.miles.cosmictools.SUCCESS
import sh.miles.cosmictools.WIPE_CACHES
import sh.miles.cosmictools.download.VersionData
import java.nio.file.Files
import java.nio.file.Path

class DeleteCachesRunStage : RunStage {

    override fun runStage(options: OptionSet, propagate: MutableMap<String, Any>): Int {
        if (!options.has(WIPE_CACHES)) return SUCCESS

        val value = options.valueOf(WIPE_CACHES)
        wipeDir(COSMIC_REACH_DECOMPILE, value)
        wipeDir(COSMIC_REACH_DOWNLOAD, value)

        return EXIT_NOW
    }

    private fun wipeDir(dir: Path, value: String?) {
        if (value == null) {
            dir.toFile().deleteRecursively()
        } else {
            val data = VersionData(value, null)
            dir.resolve(data.fileVersion).toFile().deleteRecursively()
        }
    }
}
