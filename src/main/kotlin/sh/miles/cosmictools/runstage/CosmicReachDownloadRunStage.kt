package sh.miles.cosmictools.runstage

import com.vdurmont.semver4j.Semver
import joptsimple.OptionSet
import sh.miles.cosmictools.COSMIC_REACH_DOWNLOAD
import sh.miles.cosmictools.DRIVER
import sh.miles.cosmictools.FAILURE
import sh.miles.cosmictools.IGNORE_CACHING
import sh.miles.cosmictools.IGNORE_UNOFFICIAL_SOURCE
import sh.miles.cosmictools.SOURCE
import sh.miles.cosmictools.SUCCESS
import sh.miles.cosmictools.VERSION
import sh.miles.cosmictools.download.CosmicReachSource
import sh.miles.cosmictools.download.VersionData
import sh.miles.cosmictools.utils.DownloadSource
import sh.miles.cosmictools.utils.DriverType
import java.nio.file.Files
import kotlin.system.exitProcess

class CosmicReachDownloadRunStage : RunStage {

    override fun runStage(options: OptionSet, propagate: MutableMap<String, Any>): Int {
        val source = SOURCE.value(options)
        val parameters = mutableListOf<Any>()

        if (source == null) {
            println("A SOURCE MUST BE PROVIDED")
            return FAILURE
        }

        if (!source.official && !options.has(IGNORE_UNOFFICIAL_SOURCE)) {
            println("Warning! You are Downloading Cosmic Reach From An Unofficial Source. The integrity or safety of the downloaded jar file can not be guaranteed")
            println("Press any key to acknowledge you understand")
            readlnOrNull()
        }

        setupDownloadParameters(source, options, parameters)
        val data = download(source.sourceBuilder.invoke(), options, parameters)
        propagate["version_data"] = data
        return SUCCESS
    }

    private fun download(downloader: CosmicReachSource, options: OptionSet, parameters: MutableList<Any>): VersionData {
        val data: VersionData
        downloader.use { helper ->
            helper.initialize(*parameters.toTypedArray())
            val versionsInfo = helper.info()
            val version = VERSION.value(options)
            if (version.equals("latest")) {
                data = versionsInfo.map {
                    Pair(it, Semver(semanticVersion(it.version)))
                }.maxWith(compareBy { it.second }).first
            } else {
                val tempData = versionsInfo.firstOrNull { it.version == version }
                if (tempData == null) {
                    println("This Source does not have the version $version")
                    exitProcess(FAILURE)
                }
                data = tempData
            }

            if (Files.notExists(COSMIC_REACH_DOWNLOAD.resolve(data.fileVersion)) || options.has(IGNORE_CACHING)) {
                helper.download(data, COSMIC_REACH_DOWNLOAD.resolve(data.fileVersion))
            }
        }

        return data
    }

    private fun semanticVersion(version: String): String {
        val split = version.split(".")
        val lastChar = split[2].last()
        var semanticVersion = "${split[0]}.${split[1]}."
        if (lastChar.isDigit()) {
            semanticVersion += split[2]
        } else {
            semanticVersion += "${split[2].substring(0, split[2].length - 1)}-$lastChar"
        }
        return semanticVersion
    }

    private fun setupDownloadParameters(source: DownloadSource, options: OptionSet, parameters: MutableList<Any>) {
        when (source) {
            DownloadSource.SELENIUM -> {
                if (!options.has(DRIVER)) {
                    println("A driver must be provided in order to run ")
                    exitProcess(FAILURE)
                }

                parameters.add(DRIVER.value(options) as DriverType)
            }

            DownloadSource.UNOFFICIAL_ARCHIVE_GITHUB -> {
            }
        }
    }
}
