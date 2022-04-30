package com.haeseong.nplusone.job.collector.seveneleven

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemCreateVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.job.collector.DiscountedItem
import com.haeseong.nplusone.job.collector.DiscountedItemValidator
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.concurrent.TimeUnit

open class SevenElevenCollectorTasklet : Tasklet, DiscountedItemValidator {
    @Autowired
    lateinit var itemService: ItemService

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val discountedItems = getDiscountedItems()
        validateAll(discountedItems = discountedItems)
        saveAll(discountedItems = discountedItems)
        return RepeatStatus.FINISHED
    }

    private fun getDiscountedItems(): List<DiscountedItem> {
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
            log.info("items: $items")
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
                        "https://www.7-eleven.co.kr" + it.findElement(By.cssSelector(prefix + "div > img")).getAttribute("src"),
                        DiscountType.parse(it.findElement(By.cssSelector("ul > li")).text.trim()),
                    )
                }
            log.info("items: $items")
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
                println("더 보기 버튼이 없음")
                break
            }
            try {
                if (!seeMoreButton.isDisplayed) {
                    println("더 보기 버튼이 안보임")
                    break
                }
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

    private fun saveAll(discountedItems: List<DiscountedItem>) {
        val now = LocalDate.now()
        discountedItems.forEach {
            itemService.create(
                itemCreateVo = ItemCreateVo(
                    name = it.name ?: "",
                    price = it.price ?: BigDecimal.ZERO,
                    imageUrl = it.imageUrl,
                    discountType = it.discountType,
                    storeType = StoreType.SEVEN_ELEVEN,
                    referenceDate = now,
                )
            )
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(SevenElevenCollectorTasklet::class.java)
    }
}