package com.haeseong.nplusone

import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.chrome.ChromeDriver
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

class CuTests {
    @Test
    fun paging() {
        WebDriverManager.chromedriver().setup()
        // 1. Start the session
        val driver = ChromeDriver()
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            // 2. go to CU item page
            driver.get("https://cu.bgfretail.com/event/plus.do")
            // 3. 더보기 버튼
            goToLastPage(driver)

            val items = driver.findElementsByCssSelector("#contents > div.relCon > div > ul > li")
                    .map {
                        DiscountedItem(
                                it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_text > div.name")).text,
                                it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_text > div.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_img > img")).getAttribute("src"),
                                DiscountType.parse(it.findElement(By.cssSelector("a > div.badge > span")).text.trim()),
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
            // 8. End the session
            driver.quit()
        }
    }

    private fun goToLastPage(driver: ChromeDriver) {
        while (true) {
            val seeMoreButton = try {
                driver.findElementByCssSelector("#contents > div.relCon > div > div > div.prodListBtn-w > a")
            } catch (e: NoSuchElementException) {
                println("더 보기 버튼이 없음")
                break
            }

            try {
                seeMoreButton.click()
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

data class DiscountedItem(
        val name: String?,
        val price: BigDecimal?,
        val imageUrl: String?,
        val discountType: DiscountType,
) {
    fun validate() {
        assert(!name.isNullOrBlank()) { "'name' is must not blank" }
        assert(price != null) { "'price' must not be null" }
        assert(price!! >= BigDecimal.ZERO) { "'price' must be greater than or equal to zero" }
        assert(imageUrl != null) { "'imageUrl' must not be null" }
    }
}

enum class DiscountType {
    ONE_PLUS_ONE,
    TWO_PLUS_ONE,
    UNKNOWN,
    ;

    companion object {
        fun parse(text: String): DiscountType = when (text) {
            "1+1" -> ONE_PLUS_ONE
            "2+1" -> TWO_PLUS_ONE
            else -> UNKNOWN
        }
    }
}