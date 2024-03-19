package sh.miles.cosmictools.download

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.support.ui.WebDriverWait
import sh.miles.cosmictools.COSMIC_REACH
import sh.miles.cosmictools.utils.DriverType
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration

object ItchSeleniumSource : CosmicReachSource {

    val link: String = "https://finalforeach.itch.io/cosmic-reach"

    private lateinit var driver: WebDriver
    private var initialized = false

    override fun download(version: VersionData, destination: Path) {
        if (!initialized) throw IllegalStateException("$javaClass has not yet been initialized using initialize(parameters) method")
        Files.createDirectories(COSMIC_REACH)
        val downloadButton = driver.findElements(By.className("download_btn"))
            .stream()
            .max(Comparator.comparingInt { element: WebElement ->
                element.getAttribute(
                    "data-upload_id"
                ).toInt()
            })
            .orElseThrow()
        downloadButton.click()
        WebDriverWait(driver, Duration.ofMinutes(10)).until {
            if (Files.exists(destination)) {
                return@until Files.size(destination) > 0
            }
            return@until false
        }
    }

    override fun info(): List<VersionData> {
        if (!initialized) throw IllegalStateException("$javaClass has not yet been initialized using initialize(parameters) method")
        val element = driver.findElement(By.className("version_name"))
        return listOf(VersionData(element.text.split("\\.")[1], null))
    }

    override fun initialize(vararg parameters: Any): CosmicReachSource {
        val driverType = parameters[0] as DriverType

        driver = when (driverType) {
            DriverType.CHROME -> ChromeDriver(ChromeOptions().addArguments("--headless=new"))
            DriverType.FIREFOX -> FirefoxDriver(FirefoxOptions().addArguments("--headless"))
        }

        driver.get(link)
        initialized = true
        return this
    }

    override fun close() {
        driver.quit()
    }
}
