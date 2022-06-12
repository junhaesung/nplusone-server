package com.haeseong.nplusone.job.collector.seveneleven

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResultCreateVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultDuplicatedException
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.job.collector.CollectorService
import com.haeseong.nplusone.job.collector.DiscountedItem
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class SevenElevenCollectorService(
    private val scrappingResultService: ScrappingResultService,
) : CollectorService {
    override fun getDiscountedItems(): List<DiscountedItem> {
        return getOnePlusOneItems() + getTwoPlusOneItems()
    }

    private fun getOnePlusOneItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")
        // Start the session
        val driver = ChromeDriver(chromeOptions)
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
            log.info("items(1+1).size: ${items.size}")
            return items
        } finally {
            // End the session
            driver.quit()
        }
    }

    private fun getTwoPlusOneItems(): List<DiscountedItem>  {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")
        // Start the session
        val driver = ChromeDriver(chromeOptions)
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
                        it.findElement(By.cssSelector(prefix + "div > img")).getAttribute("src"),
                        DiscountType.parse(it.findElement(By.cssSelector("ul > li")).text.trim()),
                    )
                }
            log.info("items(2+1).size: ${items.size}")
            return items
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
                log.info("더 보기 버튼이 없음")
                break
            }
            try {
                if (!seeMoreButton.isDisplayed) {
                    log.info("더 보기 버튼이 안보임")
                    break
                }
                seeMoreButton.click()
                Thread.sleep(500)
            } catch (e: InvalidElementStateException) {
                Thread.sleep(1000)
            } catch (e: StaleElementReferenceException) {
                Thread.sleep(1000)
            } catch (e: Exception) {
                log.error("알 수 없는 에러. ${e.message}")
                break
            }
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
                        storeType = StoreType.SEVEN_ELEVEN,
                        referenceDate = now,
                    )
                )
            } catch (e: ScrappingResultDuplicatedException) {
                log.warn("scrappingResult is duplicated", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(SevenElevenCollectorService::class.java)
    }
}