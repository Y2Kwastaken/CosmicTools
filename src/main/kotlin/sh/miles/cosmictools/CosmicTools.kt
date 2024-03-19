package sh.miles.cosmictools

import sh.miles.cosmictools.utils.DownloadSource

fun main(args: Array<String>) {
    val options = PARSER.parse(*args)

    if (options.has(HELP)) andSucceed { PARSER.printHelpOn(System.out) }

    val source = SOURCE.value(options)
    if (source == DownloadSource.SELENIUM && !options.has(DRIVER)) andFail { println("source type selenium requires a driver to be specified") }
    
}
