package sh.miles.cosmictools;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Predicate;
import java.util.zip.ZipInputStream;

public final class Utils {

    public static void download(@NotNull final String link, @NotNull final Path destination) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(link).openStream()); OutputStream fileOutputStream = Files.newOutputStream(destination)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unzip(@NotNull final Path zipFile, @NotNull final Path target, Predicate<String> fileFilter) {
        try {
            Files.createDirectories(target);
            try (var zip = new ZipInputStream(Files.newInputStream(zipFile))) {
                var entry = zip.getNextEntry();

                while (entry != null) {
                    if (!fileFilter.test(entry.getName())) {
                        entry = zip.getNextEntry();
                        continue;
                    }

                    Path output = target.resolve(entry.getName());

                    if (entry.isDirectory()) {
                        Files.createDirectories(output);
                        entry = zip.getNextEntry();
                        continue;
                    }

                    Files.copy(zip, output, StandardCopyOption.REPLACE_EXISTING);

                    System.out.println("Extracted: " + entry.getName());
                    entry = zip.getNextEntry();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDirectories(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void moveFile(Path source, Path dest) {
        if (Files.notExists(dest.getParent())) {
            createDirectories(dest.getParent());
        }

        try {
            Files.move(source, dest, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
