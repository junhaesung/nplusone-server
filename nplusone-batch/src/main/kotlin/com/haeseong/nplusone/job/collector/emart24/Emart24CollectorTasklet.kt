package com.haeseong.nplusone.job.collector.emart24

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemCreateVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.job.collector.DiscountedItem
import com.haeseong.nplusone.job.collector.DiscountedItemValidator
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
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
import java.time.YearMonth
import java.util.concurrent.TimeUnit

open class Emart24CollectorTasklet : Tasklet, DiscountedItemValidator {
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
//        chromeOptions.setHeadless(true)
        val driver = ChromeDriver(chromeOptions)
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
                            name = it.findElement(By.cssSelector( "div > p.productDiv")).text.trim(),
                            price = it.findElement(By.cssSelector("div > p.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                            imageUrl = it.findElement(By.cssSelector("div.box > p.productImg > img")).getAttribute("src"),
                            discountType = DiscountType.parse(it.findElement(By.cssSelector("div > div > p > img")).getAttribute("alt").replace(Regex("[\\s]"), "").take(3)),
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
            log.info("items(1+1): $items")
            return items

        } finally {
            driver.quit()
        }
    }

    private fun getTwoPlusOneItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
//        chromeOptions.setHeadless(true)
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
                            name = it.findElement(By.cssSelector( "div > p.productDiv")).text.trim(),
                            price = it.findElement(By.cssSelector("div > p.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                            imageUrl = it.findElement(By.cssSelector("div.box > p.productImg > img")).getAttribute("src"),
                            discountType = DiscountType.parse(it.findElement(By.cssSelector("div > div > p > img")).getAttribute("alt").replace(Regex("[\\s]"), "").take(3)),
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

            log.info("items(2+1): $items")
            return items
        } finally {
            driver.quit()
        }
    }

    private fun saveAll(discountedItems: List<DiscountedItem>) {
        val now = YearMonth.now()
        discountedItems.forEach {
            itemService.create(
                itemCreateVo = ItemCreateVo(
                    name = it.name ?: "",
                    price = it.price ?: BigDecimal.ZERO,
                    imageUrl = it.imageUrl,
                    discountType = it.discountType,
                    storeType = StoreType.EMART24,
                    yearMonth = now,
                )
            )
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(Emart24CollectorTasklet::class.java)
    }
}