package sh.miles.cosmictools

import joptsimple.AbstractOptionSpec
import joptsimple.ArgumentAcceptingOptionSpec
import joptsimple.OptionParser
import joptsimple.OptionSpec
import joptsimple.util.EnumConverter
import sh.miles.cosmictools.utils.DriverType
import sh.miles.cosmictools.utils.DownloadSource

val PARSER = OptionParser()

val HELP: OptionSpec<Void> = PARSER.acceptsAll(listOf("help", "h"), "The help command").forHelp()
val SOURCE: OptionSpec<DownloadSource> = PARSER.acceptsAll(listOf("source", "s", "src"), "The download source to use")
    .withRequiredArg()
    .ofType(DownloadSource::class.java)
    .withValuesConvertedBy(object : EnumConverter<DownloadSource>(DownloadSource::class.java) {})
    .defaultsTo(DownloadSource.SELENIUM)
val DRIVER: OptionSpec<DriverType> = PARSER.acceptsAll(listOf("driver", "driver-type"), "The Selenium driver to use")
    .withRequiredArg()
    .ofType(DriverType::class.java)
    .withValuesConvertedBy(object : EnumConverter<DriverType>(DriverType::class.java) {})
val VERSION: OptionSpec<String> = PARSER.acceptsAll(listOf("version", "v"), "sets the version to download")
    .withRequiredArg()
    .ofType(String::class.java)
    .defaultsTo("latest")
val IGNORE_UNOFFICIAL_SOURCE: OptionSpec<Void> =
    PARSER.acceptsAll(listOf("ignore-unofficial-source"), "Ignores the unofficial source warning")
val DECOMPILE: OptionSpec<Void> = PARSER.acceptsAll(listOf("decompile", "decompile-all"))
val NO_INSTALL: OptionSpec<Void> =
    PARSER.acceptsAll(listOf("no-install", "no-mvn-install"), "Disabled Maven Installing of the final jar")
val CURRENT_WORKING_DIRECTORY: OptionSpec<String> =
    PARSER.acceptsAll(listOf("cwd", "current-directory"), "Sets the current working directory of CosmicTools")
        .withRequiredArg()
        .ofType(String::class.java)
