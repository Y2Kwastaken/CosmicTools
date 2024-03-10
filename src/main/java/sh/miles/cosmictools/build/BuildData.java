package sh.miles.cosmictools.build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public record BuildData(@Nullable Path zip, @NotNull String version, int finalSize) {
}
