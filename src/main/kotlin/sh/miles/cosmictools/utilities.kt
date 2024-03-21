package sh.miles.cosmictools

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.function.Predicate
import java.util.zip.ZipInputStream

fun downloadTextAndFilter(link: String, filter: (String) -> Boolean): List<String> {
    val list = mutableListOf<String>()
    BufferedInputStream(URL(link).openStream()).use { bis ->
        BufferedReader(InputStreamReader(bis)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                if (filter.invoke(line)) {
                    list.add(line)
                }
                line = reader.readLine()
            }
        }
    }
    return list
}

fun downloadFile(link: String, destination: Path) {
    if (!Files.exists(destination.parent)) {
        Files.createDirectories(destination.parent)
    }
    BufferedInputStream(URL(link).openStream()).use { `in` ->
        Files.newOutputStream(destination).use { fileOutputStream ->
            val dataBuffer = ByteArray(1024)
            var bytesRead: Int
            while ((`in`.read(dataBuffer, 0, 1024).also { bytesRead = it }) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead)
            }
        }
    }
}

fun unzip(zipFile: Path, target: Path, fileFilter: Predicate<String>) {
    try {
        Files.createDirectories(target)
        ZipInputStream(Files.newInputStream(zipFile)).use { zip ->
            var entry = zip.nextEntry
            while (entry != null) {
                if (!fileFilter.test(entry.name)) {
                    entry = zip.nextEntry
                    continue
                }

                val output = target.resolve(entry.name)

                if (entry.isDirectory) {
                    Files.createDirectories(output)
                    entry = zip.nextEntry
                    continue
                }

                Files.copy(zip, output, StandardCopyOption.REPLACE_EXISTING)

                println("Extracted: " + entry.name)
                entry = zip.nextEntry
            }
        }
    } catch (e: IOException) {
        throw java.lang.RuntimeException(e)
    }
}

fun createDirectories(path: Path?) {
    try {
        Files.createDirectories(path)
    } catch (e: IOException) {
        throw java.lang.RuntimeException(e)
    }
}

fun moveFile(source: Path?, dest: Path) {
    if (Files.notExists(dest.parent)) {
        createDirectories(dest.parent)
    }

    try {
        Files.move(source, dest, StandardCopyOption.ATOMIC_MOVE)
    } catch (e: IOException) {
        throw java.lang.RuntimeException(e)
    }
}

