package sh.miles.cosmictools.build.download;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import sh.miles.cosmictools.Constants;
import sh.miles.cosmictools.build.BuildData;
import sh.miles.cosmictools.build.selenium.SeleniumManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;

/**
 * The currently only source for CosmicReach Itch.io
 */
public final class ItchSource implements DownloadSource {

    public static final String LINK = "https://finalforeach.itch.io/cosmic-reach";

    @Override
    public BuildData download(boolean download) {
        return SeleniumManager.INSTANCE.useDriver((driver) -> {
            driver.get(LINK);
            final String version = obtainVersion(driver);
            final int size = obtainSize(driver);
            Path path = null;
            if (download) {
                System.out.println("Beginning Download of Cosmic Reach");
                path = downloadJar(driver);
            } else {
                path = Constants.WORK_DOWNLOAD_PATH.resolve("v" + String.join("", version.split("\\.")));
            }

            System.out.println("Finished Download of Cosmic Reach");
            return new BuildData(path, version, size);
        });
    }

    private Path downloadJar(WebDriver driver) {
        final var dest = Constants.DOWNLOAD_PATH.resolve(Constants.COSMIC_REACH_ZIP_NAME);
        var result = driver.findElements(By.className("download_btn"))
                .stream()
                .max(
                        Comparator.comparingInt(
                                (WebElement element) -> Integer.parseInt(element.getAttribute("data-upload_id"))
                        ))
                .orElseThrow();
        result.click();
        new WebDriverWait(driver, Duration.ofMinutes(20))
                .until((d) -> {
                    try {
                        if (Files.exists(dest)) {
                            return Files.size(dest) > 0;
                        }
                        return false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        return dest;
    }

    private String obtainVersion(WebDriver driver) {
        var element = driver.findElement(By.className("version_name"));
        return element.getText().split(" ")[1];
    }

    private int obtainSize(WebDriver driver) {
        var result = driver.findElements(By.className("file_size"))
                .stream()
                .min(Comparator.comparingInt(
                        (WebElement element) -> Integer.parseInt(element.getText().split(" ")[0])
                )).orElseThrow();
        return Integer.parseInt(result.getText().split(" ")[0]);
    }
}
