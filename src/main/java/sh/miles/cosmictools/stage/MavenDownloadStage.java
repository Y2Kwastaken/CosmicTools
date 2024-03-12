package sh.miles.cosmictools.stage;

import joptsimple.OptionSet;
import org.jetbrains.annotations.NotNull;
import sh.miles.cosmictools.Utils;
import sh.miles.cosmictools.util.NeoConstants;
import sh.miles.cosmictools.NeoFlags;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class MavenDownloadStage implements RunStage {

    public static final String MAVEN_VERSION = "3.9.6";
    public static final String MAVEN_HASH = "0eb0432004a91ebf399314ad33e5aaffec3d3b29279f2f143b2f43ade26f4db7bd1c0f08e436e9445ac6dc4a564a2945d13072a160ae54a930e90581284d6461";
    public static final String MAVEN_FOLDER = "apache-maven-%s".formatted(MAVEN_VERSION);
    public static final String MAVEN_FILE = MAVEN_FOLDER + "-bin.zip";
    public static final String MAVEN_DOWNLOAD = "https://dlcdn.apache.org/maven/maven-3/" + MAVEN_VERSION + "/binaries/" + MAVEN_FILE;

    @Override
    public int runStage(@NotNull final OptionSet options, @NotNull final Map<String, Object> propagate) throws Exception {
        final Path maven = Path.of(MAVEN_FOLDER);
        final String m2 = System.getenv("M2_HOME");
        if (options.has(NeoFlags.IGNORE_CACHE)) {
            downloadMaven();
        } else if (m2 == null || !Files.exists(Path.of(m2))) {
            if (Files.notExists(maven)) {
                downloadMaven();
            }
        }

        propagate.put("maven-bin", maven.resolve("/bin/mvn"));
        return NeoConstants.SUCCESS;
    }

    public static void downloadMaven() throws IOException {
        System.out.println("Maven is not downloading, downloading maven.");
        Path tmp = Path.of(MAVEN_FILE);
        Utils.download(MAVEN_DOWNLOAD, tmp);
        Utils.unzip(tmp, NeoConstants.CWD, (s) -> true);
        Files.deleteIfExists(tmp);
        System.out.println("Finished downloading maven, maven is now downloaded.");
    }
}
