package sh.miles.cosmictools.build.selenium;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Regulates the use of selenium within the builder
 */
public final class SeleniumManager {

    public static final SeleniumManager INSTANCE = new SeleniumManager();

    private DriverType driverType = DriverType.FIREFOX;

    private SeleniumManager() {
    }

    public void setDriverType(@NotNull final DriverType driverType) {
        this.driverType = Objects.requireNonNull(driverType);
    }

    public <T> T useDriver(@NotNull final Function<WebDriver, T> driverAction) {
        Objects.requireNonNull(driverType);
        Objects.requireNonNull(driverAction);

        final WebDriver driver;
        switch (driverType) {
            case CHROME -> driver = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
            case FIREFOX -> driver = new FirefoxDriver(new FirefoxOptions().addArguments("--headless=new"));
            default -> throw new IllegalStateException("How did we get here");
        }

        T result = driverAction.apply(driver);
        driver.quit();
        return result;
    }

    public enum DriverType {
        CHROME, FIREFOX
    }

}
