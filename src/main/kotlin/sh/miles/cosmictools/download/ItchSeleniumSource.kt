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
import sh.miles.cosmictools.USER_DOWNLOAD
import sh.miles.cosmictools.USER_DOWNLOAD_COSMIC_REACH
import sh.miles.cosmictools.unzip
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
            if (Files.exists(USER_DOWNLOAD_COSMIC_REACH)) {
                return@until Files.size(USER_DOWNLOAD_COSMIC_REACH) > 0
            }
            return@until false
        }

        unzip(USER_DOWNLOAD_COSMIC_REACH, destination) { it.endsWith(".jar") }
        Files.deleteIfExists(USER_DOWNLOAD_COSMIC_REACH)
    }

    override fun info(): List<VersionData> {
        if (!initialized) throw IllegalStateException("$javaClass has not yet been initialized using initialize(parameters) method")
        val element = driver.findElement(By.className("version_name"))
        println("Finding Version Info for Cosmic Reach")
        val list = listOf(VersionData(element.text.split(" ")[1], null))
        println("Finished finding Version Info for Cosmic Reach")
        return list
    }

    override fun initialize(vararg parameters: Any): CosmicReachSource {
        val driverType = parameters[0] as DriverType

        driver = when (driverType) {
            DriverType.CHROME -> ChromeDriver(ChromeOptions().addArguments("--headless=new"))
            DriverType.FIREFOX -> FirefoxDriver(FirefoxOptions().addArguments(""))
        }

        driver.get(link)
        initialized = true
        return this
    }

    override fun close() {
        driver.quit()
    }
}
