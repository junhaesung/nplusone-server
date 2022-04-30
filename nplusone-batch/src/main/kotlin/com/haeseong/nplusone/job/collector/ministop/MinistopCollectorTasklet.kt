package com.haeseong.nplusone.job.collector.ministop

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemCreateVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.job.collector.DiscountedItem
import com.haeseong.nplusone.job.collector.DiscountedItemValidator
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.NoSuchElementException
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

open class MinistopCollectorTasklet : Tasklet, DiscountedItemValidator {
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

    private fun saveAll(discountedItems: List<DiscountedItem>) {
        val now = LocalDate.now()
        discountedItems.forEach {
            itemService.create(
                itemCreateVo = ItemCreateVo(
                    name = it.name ?: "",
                    price = it.price ?: BigDecimal.ZERO,
                    imageUrl = it.imageUrl,
                    discountType = it.discountType,
                    storeType = StoreType.MINISTOP,
                    referenceDate = now,
                )
            )
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(MinistopCollectorTasklet::class.java)
    }
}