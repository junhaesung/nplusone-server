package com.haeseong.nplusone.job.collector.ministop

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResultCreateVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultDuplicatedException
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.job.collector.CollectorService
import com.haeseong.nplusone.job.collector.DiscountedItem
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class MinistopCollectorService(
    private val scrappingResultService: ScrappingResultService,
) : CollectorService {
    override fun getDiscountedItems(): List<DiscountedItem> {
        return getOnePlusOneItems() + getTwoPlusOneItems()
    }

    private fun getOnePlusOneItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking"));
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")

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
            log.info("items(1+1): $items")
            return items
        } finally {
            // End the session
            driver.quit()
        }
    }

    private fun getTwoPlusOneItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setExperimentalOption("excludeSwitches", listOf("disable-popup-blocking"))
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")

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
            log.info("items(2+1): $items")
            return items
        } finally {
            // End the session
            driver.quit()
        }
    }

    override fun saveAll(discountedItems: List<DiscountedItem>) {
        val now = LocalDate.now()
        discountedItems.forEach {
            try {
                scrappingResultService.create(
                    scrappingResultCreateVo = ScrappingResultCreateVo(
                        name = it.name ?: "",
                        price = it.price ?: BigDecimal.ZERO,
                        imageUrl = it.imageUrl,
                        discountType = it.discountType,
                        storeType = StoreType.MINISTOP,
                        referenceDate = now,
                    )
                )
            } catch (e: ScrappingResultDuplicatedException) {
                log.warn("scrappingResult is duplicated", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(MinistopCollectorService::class.java)
    }
}