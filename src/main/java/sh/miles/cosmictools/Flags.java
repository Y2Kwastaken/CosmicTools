package sh.miles.cosmictools;

import joptsimple.OptionParser;
import joptsimple.OptionSpec;
import joptsimple.util.EnumConverter;
import sh.miles.cosmictools.build.selenium.SeleniumManager;

public final class Flags {

    public static final OptionParser PARSER = new OptionParser();

    public static final OptionSpec<Void> HELP_FLAG = PARSER.accepts("help", "Displays the help command");
    public static final OptionSpec<Void> NO_DOWNLOAD_COSMIC = PARSER.accepts("nodown-cosmic", "Doesn't download cosmic from itch.io");
    public static final OptionSpec<Void> NO_DOWNLOAD_VINEFLOWER = PARSER.accepts("nodown-vineflower", "Doesn't download vineflower");
    public static final OptionSpec<Void> DOWNLOAD_ONLY = PARSER.accepts("download-only", "Only downloads Cosmic Reach but does not decompile it");
    public static final OptionSpec<SeleniumManager.DriverType> DRIVER_TYPE_OPTION = PARSER.accepts("driver-type", "The Selenium DriverType to use")
            .requiredUnless(HELP_FLAG)
            .withRequiredArg()
            .ofType(SeleniumManager.DriverType.class)
            .withValuesConvertedBy(new EnumConverter<>(SeleniumManager.DriverType.class) {
            });

}
