package sh.miles.cosmictools.util;

import java.nio.file.Path;

public final class CurrentDirectory {

    private static Path CWD = Path.of(".");

    public static Path cwd() {
        return CWD;
    }

    public static void cwd(Path path) {
        CWD = path;
    }

}
