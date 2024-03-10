package sh.miles.cosmictools;

import joptsimple.OptionSet;
import sh.miles.cosmictools.build.BuildData;
import sh.miles.cosmictools.build.download.DownloadSource;

import java.io.IOException;

/**
 * The entry point for CosmicTools
 */
public class Main {

    /**
     * CLI Entry points
     *
     * @param args args
     */
    public static void main(String[] args) throws IOException {
        final OptionSet options = Flags.PARSER.parse(args);

        if (options.has(Flags.HELP_FLAG)) {
            Flags.PARSER.printHelpOn(System.out);
            System.exit(0);
        }

        Utils.createDirectories(Constants.WORK_PATH);
        boolean downloadCosmic = !options.has(Flags.NO_DOWNLOAD_COSMIC);
        final BuildData data = DownloadSource.newPreferredSource().download(downloadCosmic);

        if (!options.has(Flags.NO_DOWNLOAD_VINEFLOWER)) {
            System.out.println("Beginning Download of VineFlower");
            Utils.download(Constants.VINE_FLOWER_DOWNLOAD_LINK, Constants.WORK_PATH.resolve("vineflower.jar"));
        }

        var workDownloadsCosmicZip = Constants.getDownloadPath(data);
        if (downloadCosmic) {
            Utils.moveFile(data.zip(), workDownloadsCosmicZip.resolve(Constants.COSMIC_REACH_ZIP_NAME));
            Utils.unzip(workDownloadsCosmicZip.resolve(Constants.COSMIC_REACH_ZIP_NAME), workDownloadsCosmicZip, (s) -> !s.equalsIgnoreCase(".itch.toml"));
        }

        if (options.has(Flags.DOWNLOAD_ONLY)) {
            System.exit(0);
        }

        var jarPath = Constants.getCosmicReachJarPath(data);
        var decompilePath = Constants.getDecompilePath(data);
        Utils.unzip(jarPath, decompilePath.resolve("classes"), (s) -> s.startsWith("finalforeach/cosmicreach"));
        try {
            new ProcessBuilder()
                    .command("java", "-jar", "work/vineflower.jar", "-dgs=1", "-hdc=0", "-rbr=0", "-asc=1", "-udc=0",
                            decompilePath.resolve("classes").toString(), decompilePath.toString())
                    .inheritIO()
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    private static void mainFullRunThrough() {
        Utils.createDirectories(Constants.WORK_PATH);

        BuildData data = DownloadSource.newPreferredSource().download(true);
        Utils.download(Constants.VINE_FLOWER_DOWNLOAD_LINK, Constants.WORK_PATH.resolve("vineflower.jar"));
        var zipPath = Constants.getDownloadPath(data);
        Utils.moveFile(data.zip(), zipPath.resolve(Constants.COSMIC_REACH_ZIP_NAME));
        Utils.unzip(zipPath.resolve(Constants.COSMIC_REACH_ZIP_NAME), zipPath, (s) -> !s.equalsIgnoreCase(".itch.toml"));
        var jarPath = Constants.getCosmicReachJarPath(data);
        var decompilePath = Constants.getDecompilePath(data);
        Utils.unzip(jarPath, decompilePath.resolve("classes"), (s) -> s.startsWith("finalforeach/cosmicreach"));

        try {
            new ProcessBuilder()
                    .command("java", "-jar", "work/vineflower.jar", "-dgs=1", "-hdc=0", "-rbr=0", "-asc=1", "-udc=0",
                            decompilePath.resolve("classes").toString(), decompilePath.toString())
                    .inheritIO()
                    .start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

