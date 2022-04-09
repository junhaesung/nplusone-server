package com.haeseong.nplusone

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.util.concurrent.TimeUnit


class MinistopTests {
    @Test
    fun onePlusOne() {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking"));

        // Start the session
        val driver = ChromeDriver(chromeOptions)
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        val items = mutableListOf<DiscountedItem>()

        try {
            driver.get("https://www.ministop.co.kr/MiniStopHomePage/page/event/plus1.do")
            Thread.sleep(1000)

            // 20개씩 저장
            var sequence = 1
            while (true) {
                val target = try {
                    driver.findElementByCssSelector("#section > div.inner.wrap.service1 > div.event_plus_list > ul > li:nth-child($sequence)")
                } catch (e: NoSuchElementException) {
                    break
                }

                val split = target.findElement(By.cssSelector("a > p")).text.split("\n").map { it.trim() }
                val discountedItem = DiscountedItem(
                        split[0],
                        split[1].replace(Regex("[,원\\s]"), "").toBigDecimal(),
                        target.findElement(By.cssSelector("a > img")).getAttribute("src"),
                        DiscountType.parse(target.findElement(By.cssSelector("a > span")).text.trim()),
                )
                items.add(discountedItem)

                if (sequence % 20 == 0) {
                    // see more
                    val seeMoreButton = driver.findElementByCssSelector("#section > div.inner.wrap.service1 > div.event_plus_list > div > a.pr_more")
                    seeMoreButton.click()
                    Thread.sleep(1000)
                }

                sequence += 1
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
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking"));

        // Start the session
        val driver = ChromeDriver(chromeOptions)
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        val items = mutableListOf<DiscountedItem>()

        try {
            driver.get("https://www.ministop.co.kr/MiniStopHomePage/page/event/plus2.do")
            Thread.sleep(1000)

            // 20개씩 저장
            var sequence = 1
            while (true) {
                val target = try {
                    driver.findElementByCssSelector("#section > div.inner.wrap.service1 > div.event_plus_list > ul > li:nth-child($sequence)")
                } catch (e: NoSuchElementException) {
                    break
                }

                val split = target.findElement(By.cssSelector("a > p")).text.split("\n").map { it.trim() }
                val discountedItem = DiscountedItem(
                        split[0],
                        split[1].replace(Regex("[,원\\s]"), "").toBigDecimal(),
                        target.findElement(By.cssSelector("a > img")).getAttribute("src"),
                        DiscountType.parse(target.findElement(By.cssSelector("a > span")).text.trim()),
                )
                items.add(discountedItem)

                if (sequence % 20 == 0) {
                    // see more
                    val seeMoreButton = driver.findElementByCssSelector("#section > div.inner.wrap.service1 > div.event_plus_list > div > a.pr_more")
                    seeMoreButton.click()
                    Thread.sleep(1000)
                }

                sequence += 1
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
}