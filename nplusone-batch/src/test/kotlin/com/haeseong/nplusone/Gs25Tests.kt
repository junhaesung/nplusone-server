package com.haeseong.nplusone

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.DiscountedItem
import io.github.bonigarcia.wdm.WebDriverManager
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.chrome.ChromeDriver
import java.util.concurrent.TimeUnit

@Disabled
class Gs25Tests {
    @Test
    fun paging() {
        WebDriverManager.chromedriver().setup()
        // 1. Start the session
        val driver = ChromeDriver()
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            // 2. go to GS25 item page (행사상품)
            driver.get("http://gs25.gsretail.com/gscvs/ko/products/event-goods")
            val items = mutableListOf<DiscountedItem>()

            // 1+1 상품 조회
            while (true) {
                val discountedItems = driver.findElementsByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(3) > ul > li")
                        .map {
                            DiscountedItem(
                                    it.findElement(By.cssSelector("div.prod_box > p.tit")).text,
                                    it.findElement(By.cssSelector("div.prod_box > p.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                    it.findElement(By.cssSelector("div.prod_box > p.img > img")).getAttribute("src"),
                                    DiscountType.parse(it.findElement(By.cssSelector("div.prod_box > div > p > span")).text.trim()),
                            )
                        }
                items.addAll(discountedItems)

                try {
                    val nextButton = driver.findElementByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(3) > div > a[onclick].next")
                    nextButton.click()
                    Thread.sleep(1000)
                } catch (e: NoSuchElementException) {
                    println("더 보기 버튼이 없음")
                    break
                }
            }

            // 2+1 으로 이동
            driver.findElementByCssSelector("#TWO_TO_ONE").click()
            Thread.sleep(1000)

            // 2+1 상품 조회
            while (true) {
                val discountedItems = driver.findElementsByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(5) > ul > li")
                        .map {
                            DiscountedItem(
                                    it.findElement(By.cssSelector("div.prod_box > p.tit")).text,
                                    it.findElement(By.cssSelector("div.prod_box > p.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                                    it.findElement(By.cssSelector("div.prod_box > p.img > img")).getAttribute("src"),
                                    DiscountType.parse(it.findElement(By.cssSelector("div.prod_box > div > p > span")).text.trim()),
                            )
                        }
                items.addAll(discountedItems)

                try {
                    val nextButton = driver.findElementByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(5) > div > a[onclick].next")
                    nextButton.click()
                    Thread.sleep(1000)
                } catch (e: NoSuchElementException) {
                    println("더 보기 버튼이 없음")
                    break
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
            // 8. End the session
            driver.quit()
        }
    }
}