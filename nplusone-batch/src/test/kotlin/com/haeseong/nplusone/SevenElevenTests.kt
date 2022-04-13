package com.haeseong.nplusone

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.job.collector.DiscountedItem
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit

@Disabled
class SevenElevenTests {
    @Test
    fun onePlusOne() {
        WebDriverManager.chromedriver().setup()
        // Start the session
        val driver = ChromeDriver()
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            driver.get("https://www.7-eleven.co.kr/product/presentList.asp")
            // 1+1
            driver.executeScript("javascript: fncTab(1)")
            Thread.sleep(1)
            // load all
            goToLastPage(driver)
            Thread.sleep(1)
            // save all
            val items = driver.findElementsByCssSelector("#listUl > li")
                    .drop(1)
                    .filter { it.isDisplayed }
                    .mapIndexed { index, it ->
                        val prefix = if (index >= 14) "div > " else ""
                        try {
                            DiscountedItem(
                                    it.findElement(By.cssSelector( prefix + "div > div > div.name")).text,
                                    it.findElement(By.cssSelector(prefix + "div > div > div.price > span")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                    it.findElement(By.cssSelector(prefix + "div > img")).getAttribute("src"),
                                    DiscountType.parse(it.findElement(By.cssSelector("ul > li")).text.trim()),
                            )
                        } catch (e: Exception) {
                            println("Failed to get DiscountedItem. index: $index, message: ${e.message}")
                            throw e
                        }
                    }
            println("items: $items")

            val invalidItems = items.filter {
                try {
                    it.validate()
                    false
                } catch (e: Exception) {
                    true
                }
            }
            println("invalid items: $invalidItems")

        } finally {
            // End the session
            driver.quit()
        }
    }

    @Test
    fun twoPlusOne() {
        WebDriverManager.chromedriver().setup()
        // Start the session
        val driver = ChromeDriver()
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            driver.get("https://www.7-eleven.co.kr/product/presentList.asp")
            // 2+1
            driver.executeScript("javascript: fncTab(2)")
            Thread.sleep(1)
            // load all
            goToLastPage(driver)
            Thread.sleep(1)
            // save all
            val items = driver.findElementsByCssSelector("#listUl > li")
                    .drop(1)
                    .filter { it.isDisplayed }
                    .mapIndexed { index, it ->
                        val prefix = if (index >= 14) "div > " else ""
                        DiscountedItem(
                                it.findElement(By.cssSelector( prefix + "div > div > div.name")).text,
                                it.findElement(By.cssSelector(prefix + "div > div > div.price > span")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                "https://www.7-eleven.co.kr" + it.findElement(By.cssSelector(prefix + "div > img")).getAttribute("src"),
                                DiscountType.parse(it.findElement(By.cssSelector("ul > li")).text.trim()),
                        )
                    }
            println("items: $items")

            val invalidItems = items.filter {
                try {
                    it.validate()
                    false
                } catch (e: Exception) {
                    true
                }
            }
            println("invalid items: $invalidItems")

        } finally {
            // End the session
            driver.quit()
        }
    }

    private fun goToLastPage(driver: ChromeDriver) {
        while (true) {
            val seeMoreButton = try {
                driver.findElementByClassName("btn_more")
            } catch (e: NoSuchElementException) {
                println("더 보기 버튼이 없음")
                break
            }
            if (!seeMoreButton.isDisplayed) {
                println("더 보기 버튼이 안보임")
                break
            }

            try {
                seeMoreButton.click()
                Thread.sleep(500)
            } catch (e: InvalidElementStateException) {
                Thread.sleep(1000)
            } catch (e: StaleElementReferenceException) {
                Thread.sleep(1000)
            } catch (e: Exception) {
                println("알 수 없는 에러. ${e.message}")
                break
            }
        }
    }
}