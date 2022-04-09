package com.haeseong.nplusone

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit

@Disabled
class SeleniumTests {
    @Test
    fun hello() {
        WebDriverManager.chromedriver().setup()
        // 1. Start the session
        val driver = ChromeDriver()
        // 2. Take action on browser
        driver.get("https://google.com")
        // 3. Request browser information
        driver.title
        // 4. Establish Waiting Strategy
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS)
        // 5. Find an element
        val searchBox = driver.findElement(By.name("q"))
        val searchButton = driver.findElement(By.name("btnK"))
        // 6. Take action on element
        searchBox.sendKeys("Selenium")
        searchButton.click()
        // 7. Request element information
        driver.findElement(By.name("q")).getAttribute("value")
        // 8. End the session
        driver.quit()
    }
}