package sh.miles.cosmictools.cosmicreach;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import sh.miles.cosmictools.util.CosmicReachInfo;
import sh.miles.cosmictools.util.DriverType;
import sh.miles.cosmictools.util.NeoConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Comparator;

class ItchSeleniumSource implements CosmicReachSource {

    public static final String ITCH_LINK = "https://finalforeach.itch.io/cosmic-reach";

    static ItchSeleniumSource INSTANCE = new ItchSeleniumSource();
    private DriverType driverType;
    private WebDriver driver = null;

    @Override
    public void download(@NotNull final CosmicReachInfo data) throws IOException {
        Files.createDirectories(NeoConstants.COSMIC_REACH);
        initializeDriver();
        final Path destination = NeoConstants.USER_DOWNLOAD_COSMIC_REACH;
        WebElement downloadButton = driver.findElements(By.className("download_btn"))
                .stream()
                .max(Comparator.comparingInt((WebElement element) -> Integer.parseInt(element.getAttribute("data-upload_id"))))
                .orElseThrow();
        downloadButton.click();
        new WebDriverWait(driver, Duration.ofMinutes(10))
                .until((d) -> {
                    try {
                        if (Files.exists(destination)) {
                            return Files.size(destination) > 0;
                        }
                        return false;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CosmicReachInfo retrieveBuildData() {
        initializeDriver();
        var element = driver.findElement(By.className("version_name"));
        return new CosmicReachInfo(element.getText().split(" ")[1]);
    }

    private void initializeDriver() {
        if (driver != null) {
            return;
        }

        switch (driverType) {
            case CHROME -> driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
            case FIREFOX -> driver = new FirefoxDriver(new FirefoxOptions().addArguments("--headless"));
            default -> throw new IllegalStateException("How did we get here");
        }

        driver.get(ITCH_LINK);
    }

    @Override
    public void close() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

    public void setDriverType(final DriverType driverType) {
        this.driverType = driverType;
    }
}
