package sh.miles.cosmictools.util;

import java.nio.file.Path;

public class NeoConstants {

    public static final Path CWD = Path.of(".");
    public static final Path USER_DOWNLOAD = Path.of(System.getProperty("user.home"), "Downloads");
    public static final Path USER_DOWNLOAD_COSMIC_REACH = USER_DOWNLOAD.resolve("cosmic-reach-jar.zip");
    public static final Path COSMIC_REACH = CWD.resolve("cosmic-reach");
    public static final Path COSMIC_REACH_DOWNLOAD = COSMIC_REACH.resolve("download");
    public static final Path COSMIC_REACH_ZIP_PATH = COSMIC_REACH.resolve("cosmic-reach-jar.zip");
    public static final Path VINE_FLOWER = CWD.resolve("vineflower.jar");

    public static final int SUCCESS = 0;
    public static final int FAILURE = 1;

}
