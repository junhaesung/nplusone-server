package com.haeseong.nplusone.job.collector.emart24

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemDuplicatedException
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResultCreateVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.job.collector.DiscountedItem
import com.haeseong.nplusone.job.collector.DiscountedItemValidator
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.TimeUnit

interface Emart24CollectorService : DiscountedItemValidator {
    fun getDiscountedItems(): List<DiscountedItem>
    fun saveAll(discountedItems: List<DiscountedItem>)
}

class Emart24CollectorServiceImpl(
    private val scrappingResultService: ScrappingResultService,
) : Emart24CollectorService {
    override fun getDiscountedItems(): List<DiscountedItem> {
        return getOnePlusOneItems() + getTwoPlusOneItems()
    }

    private fun getOnePlusOneItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--window-size=1920x1080", // headless 에서 click 사용하려면 필요함
        )
        val driver = ChromeDriver(chromeOptions)
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)

        val items = mutableListOf<DiscountedItem>()

        try {
            driver.get("https://emart24.co.kr/product/eventProduct.asp")
            driver.executeScript("javascript:goTab('1n1')")

            while (true) {
                // save all
                val discountedItems = driver.findElementsByCssSelector("#regForm > div.section > div.eventProduct > div.tabContArea > ul > li")
                    .map {
                        val priceText = it.findElement(By.cssSelector("div > p.price")).text.replace(Regex("[,원\\s]"), "")
                        DiscountedItem(
                            name = it.findElement(By.cssSelector( "div > p.productDiv")).text.trim(),
                            price = if (priceText.isBlank()) BigDecimal.ZERO else priceText.toBigDecimal(),
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
            log.info("items(1+1).size: ${items.size}")
            return items

        } finally {
            driver.quit()
        }
    }

    private fun getTwoPlusOneItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments(
            "--no-sandbox",
            "--disable-dev-shm-usage",
            "--window-size=1920x1080",
        )
        val driver = ChromeDriver(chromeOptions)
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)

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

            log.info("items(2+1).size: ${items.size}")
            return items
        } finally {
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
                        storeType = StoreType.EMART24,
                        referenceDate = now,
                    )
                )
            } catch (e: ItemDuplicatedException) {
                log.warn("Item duplicated", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(Emart24CollectorServiceImpl::class.java)
    }
}