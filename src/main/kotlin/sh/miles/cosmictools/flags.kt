package sh.miles.cosmictools

import joptsimple.OptionParser
import joptsimple.util.EnumConverter
import sh.miles.cosmictools.utils.DriverType
import sh.miles.cosmictools.utils.DownloadSource

val PARSER = OptionParser()

val HELP = PARSER.acceptsAll(listOf("help", "h"), "The help command").forHelp()
val SOURCE = PARSER.acceptsAll(listOf("source", "s", "src"), "The download source to use")
    .withRequiredArg()
    .ofType(DownloadSource::class.java)
    .withValuesConvertedBy(object : EnumConverter<DownloadSource>(DownloadSource::class.java) {})
    .defaultsTo(DownloadSource.SELENIUM)
val DRIVER = PARSER.acceptsAll(listOf("driver", "driver-type"), "The Selenium driver to use")
    .withRequiredArg()
    .ofType(DriverType::class.java)
    .withValuesConvertedBy(object : EnumConverter<DriverType>(DriverType::class.java) {})
