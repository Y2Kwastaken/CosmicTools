package sh.miles.cosmictools

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

fun downloadTextAndFilter(link: String, filter: (String) -> Boolean): List<String> {
    val list = mutableListOf<String>()
    BufferedInputStream(URL(link).openStream()).use { bis ->
        BufferedReader(InputStreamReader(bis)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {

                line = reader.readLine()
            }
        }
    }
    return list
}

fun downloadFile(link: String, destination: Path) {
    try {
        BufferedInputStream(URL(link).openStream()).use { `in` ->
            Files.newOutputStream(destination).use { fileOutputStream ->
                val dataBuffer = ByteArray(1024)
                var bytesRead: Int
                while ((`in`.read(dataBuffer, 0, 1024).also { bytesRead = it }) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead)
                }
            }
        }
    } catch (e: IOException) {
        throw RuntimeException(e)
    }
}
