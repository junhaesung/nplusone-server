package com.haeseong.nplusone.job.collector.emart24

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResultCreateVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultDuplicatedException
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.job.collector.CollectorService
import com.haeseong.nplusone.job.collector.DiscountedItem
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class Emart24CollectorService(
    private val scrappingResultService: ScrappingResultService,
) : CollectorService {
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
            driver.get("https://www.emart24.co.kr/goods/event?search=&category_seq=1&align=")

            while (true) {
                // save all
                val discountedItems = driver.findElementsByCssSelector("body > div.viewContentsWrap > div > section.itemList.active > div")
                    .map {
                        val priceText = it.findElement(By.cssSelector("div.itemTxtWrap > span")).text.replace(Regex("[,원\\s]"), "")
                        DiscountedItem(
                            name = it.findElement(By.cssSelector( "div.itemTxtWrap > div.itemtitle > p")).text.trim(),
                            price = if (priceText.isBlank()) BigDecimal.ZERO else priceText.toBigDecimal(),
                            imageUrl = it.findElement(By.cssSelector("div.itemImg > img")).getAttribute("src"),
                            discountType = DiscountType.parse(it.findElement(By.cssSelector("div.itemTit > span.onepl.floatR")).text.replace(Regex("[\\s]"), "").take(3)),
                        )
                    }
                items.addAll(discountedItems)

                val nextButton = driver.findElementByCssSelector("body > div.viewContentsWrap > div > div > div.nextButtons > div")
                if (nextButton.getAttribute("style").contains("opacity")) {
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
            driver.get("https://www.emart24.co.kr/goods/event?search=&category_seq=2&align=")

            while (true) {
                // save all
                val discountedItems = driver.findElementsByCssSelector("body > div.viewContentsWrap > div > section.itemList.active > div")
                    .map {
                        val priceText = it.findElement(By.cssSelector("div.itemTxtWrap > span")).text.replace(Regex("[,원\\s]"), "")
                        DiscountedItem(
                            name = it.findElement(By.cssSelector( "div.itemTxtWrap > div.itemtitle > p")).text.trim(),
                            price = if (priceText.isBlank()) BigDecimal.ZERO else priceText.toBigDecimal(),
                            imageUrl = it.findElement(By.cssSelector("div.itemImg > img")).getAttribute("src"),
                            discountType = DiscountType.parse(it.findElement(By.cssSelector("div.itemTit > span.twopl.floatR")).text.replace(Regex("[\\s]"), "").take(3)),
                        )
                    }
                items.addAll(discountedItems)

                val nextButton = driver.findElementByCssSelector("body > div.viewContentsWrap > div > div > div.nextButtons > div")
                if (nextButton.getAttribute("style").contains("opacity")) {
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
            } catch (e: ScrappingResultDuplicatedException) {
                log.warn("scrappingResult is duplicated", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(Emart24CollectorService::class.java)
    }
}