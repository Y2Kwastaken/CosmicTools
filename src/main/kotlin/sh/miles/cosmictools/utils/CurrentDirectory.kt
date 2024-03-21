package sh.miles.cosmictools.utils

import java.nio.file.Path

object CurrentDirectory {

    private var CWD: Path = Path.of(".")

    fun cwd(): Path {
        return CWD
    }

    fun cwd(path: Path) {
        CWD = path
    }
}
