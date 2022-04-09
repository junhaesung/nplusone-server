package com.haeseong.nplusone

import com.haeseong.nplusone.domain.DiscountType
import com.haeseong.nplusone.domain.DiscountedItem
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit

@Disabled
class Emart24Tests {
    @Test
    fun onePlusOne() {
        WebDriverManager.chromedriver().setup()
        // Start the session
        val driver = ChromeDriver()
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        val items = mutableListOf<DiscountedItem>()

        try {
            driver.get("https://emart24.co.kr/product/eventProduct.asp")
            driver.executeScript("javascript:goTab('1n1')")

            while (true) {
                // save all
                val discountedItems = driver.findElementsByCssSelector("#regForm > div.section > div.eventProduct > div.tabContArea > ul > li")
                        .map {
                            DiscountedItem(
                                    it.findElement(By.cssSelector( "div > p.productDiv")).text.trim(),
                                    it.findElement(By.cssSelector("div > p.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                    it.findElement(By.cssSelector("div.box > p.productImg > img")).getAttribute("src"),
                                    DiscountType.parse(it.findElement(By.cssSelector("div > div > p > img")).getAttribute("alt").replace(Regex("[\\s]"), "").take(3)),
                            )
                        }
                items.addAll(discountedItems)

                val nextButton = driver.findElementByCssSelector("#regForm > div.section > div.eventProduct > div.paging > a.next.bgNone")
                if (nextButton.getAttribute("href").endsWith("#none")) {
                    break
                } else {
                    nextButton.click()
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

        val items = mutableListOf<DiscountedItem>()

        try {
            driver.get("https://emart24.co.kr/product/eventProduct.asp")
            driver.executeScript("javascript:goTab('2n1')")

            while (true) {
                // save all
                val discountedItems = driver.findElementsByCssSelector("#regForm > div.section > div.eventProduct > div.tabContArea > ul > li")
                        .map {
                            DiscountedItem(
                                    it.findElement(By.cssSelector( "div > p.productDiv")).text.trim(),
                                    it.findElement(By.cssSelector("div > p.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                    it.findElement(By.cssSelector("div.box > p.productImg > img")).getAttribute("src"),
                                    DiscountType.parse(it.findElement(By.cssSelector("div > div > p > img")).getAttribute("alt").replace(Regex("[\\s]"), "").take(3)),
                            )
                        }
                items.addAll(discountedItems)

                val nextButton = driver.findElementByCssSelector("#regForm > div.section > div.eventProduct > div.paging > a.next.bgNone")
                if (nextButton.getAttribute("href").endsWith("#none")) {
                    break
                } else {
                    nextButton.click()
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
}