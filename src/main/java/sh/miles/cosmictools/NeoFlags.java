package sh.miles.cosmictools;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.util.EnumConverter;
import joptsimple.util.PathConverter;
import sh.miles.cosmictools.util.DriverType;

import java.nio.file.Path;
import java.util.List;

public class NeoFlags {

    public static final OptionParser PARSER = new OptionParser();

    public static final OptionSpec<Void> HELP = PARSER.accepts("help", "Prints All Help Options")
            .forHelp();
    public static final OptionSpec<DriverType> DRIVER = PARSER.acceptsAll(List.of("driver", "driver-type"), "The Selenium DriverType to use")
            .requiredUnless(HELP)
            .withRequiredArg()
            .ofType(DriverType.class)
            .withValuesConvertedBy(new EnumConverter<>(DriverType.class) {
            });
    public static final OptionSpec<Path> CURRENT_DIRECTORY = PARSER.acceptsAll(List.of("cwd", "current-directory", "curdir"), "Sets the current working directory of CosmicTools")
            .withRequiredArg()
            .ofType(String.class)
            .withValuesConvertedBy(new PathConverter());
    public static final OptionSpec<Void> NO_INSTALL = PARSER.accepts("no-install", "does not maven install Cosmic Reach");
    public static final OptionSpec<Void> DECOMPILE = PARSER.accepts("decompile", "decompiles Cosmic Reach");
    public static final OptionSpec<Void> IGNORE_CACHE = PARSER.accepts("ignore-cache", "ignores all caches values and hard downloads all elements even if caches");
    public static final OptionSpec<Void> CLEAR_CACHE = PARSER.accepts("clear-cache", "clears all caching done");

}
