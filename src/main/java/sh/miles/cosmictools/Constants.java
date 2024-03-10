package sh.miles.cosmictools;

import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.build.BuildData;

import java.nio.file.Path;

/**
 * A Class full of constants used by CosmicTools
 */
public final class Constants {

    public static final String VINE_FLOWER_DOWNLOAD_LINK = "https://github.com/Vineflower/vineflower/releases/download/1.9.3/vineflower-1.9.3.jar";
    public static final String COSMIC_REACH_ZIP_NAME = "cosmic-reach-jar.zip";

    public static final Path DOWNLOAD_PATH = Path.of(System.getProperty("user.home"), "Downloads");

    public static final Path CWD = Path.of(".");
    public static final Path WORK_PATH = CWD.resolve("work");
    public static final Path DECOMPILE_PATH = WORK_PATH.resolve("decompile");
    public static final Path WORK_DOWNLOAD_PATH = WORK_PATH.resolve("downloads");

    public static Path getDownloadPath(@NotNull final BuildData data) {
        return WORK_DOWNLOAD_PATH.resolve("v" + String.join("", data.version().split("\\.")));
    }

    public static Path getCosmicReachJarPath(@NotNull final BuildData data) {
        return getDownloadPath(data).resolve("Cosmic Reach-" + data.version() + ".jar");
    }

    public static Path getDecompilePath(@NotNull final BuildData data) {
        return DECOMPILE_PATH.resolve("v" + String.join("", data.version().split("\\.")));
    }


}
