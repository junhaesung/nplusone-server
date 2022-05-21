package com.haeseong.nplusone.job.collector.gs25

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.ItemDuplicatedException
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.domain.scrapping.ScrappingResultCreateVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.job.collector.DiscountedItem
import com.haeseong.nplusone.job.collector.DiscountedItemValidator
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

interface Gs25CollectorService : DiscountedItemValidator {
    fun getDiscountedItems(): List<DiscountedItem>
    fun saveAll(discountedItems: List<DiscountedItem>)
}

class Gs25CollectorServiceImpl(
    private val scrappingResultService: ScrappingResultService,
) : Gs25CollectorService {
    override fun getDiscountedItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")
        val driver = ChromeDriver(chromeOptions)
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            // go to GS25 item page (행사상품)
            driver.get("http://gs25.gsretail.com/gscvs/ko/products/event-goods")
            val items = mutableListOf<DiscountedItem>()

            // 1+1 상품 조회
            while (true) {
                val discountedItems = driver.findElementsByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(3) > ul > li")
                    .map {
                        DiscountedItem(
                            name = it.findElement(By.cssSelector("div.prod_box > p.tit")).text,
                            price = it.findElement(By.cssSelector("div.prod_box > p.price")).text.replace(Regex("[,원\\s]"),
                                "").toBigDecimal(),
                            imageUrl = try {
                                it.findElement(By.cssSelector("div.prod_box > p.img > img")).getAttribute("src")
                            } catch (e: NoSuchElementException) {
                                null
                            },
                            discountType = DiscountType.parse(it.findElement(By.cssSelector("div.prod_box > div > p > span")).text.trim()),
                        )
                    }
                items.addAll(discountedItems)

                try {
                    val nextButton = driver.findElementByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(3) > div > a[onclick].next")
                    nextButton.click()
                    Thread.sleep(1000)
                } catch (e: NoSuchElementException) {
                    log.info("더 보기 버튼이 없음 (1+1)")
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
                            name = it.findElement(By.cssSelector("div.prod_box > p.tit")).text,
                            price = it.findElement(By.cssSelector("div.prod_box > p.price")).text.replace(Regex("[,원\\s]"),
                                "").toBigDecimal(),
                            imageUrl = try {
                                it.findElement(By.cssSelector("div.prod_box > p.img > img")).getAttribute("src")
                            } catch (e: NoSuchElementException) {
                                null
                            },
                            discountType = DiscountType.parse(it.findElement(By.cssSelector("div.prod_box > div > p > span")).text.trim()),
                        )
                    }
                items.addAll(discountedItems)

                try {
                    val nextButton = driver.findElementByCssSelector("#contents > div.cnt > div.cnt_section.mt50 > div > div > div:nth-child(5) > div > a[onclick].next")
                    nextButton.click()
                    Thread.sleep(1000)
                } catch (e: NoSuchElementException) {
                    log.info("더 보기 버튼이 없음 (2+1)")
                    break
                }
            }
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
                        imageUrl = it.imageUrl ?: NO_IMAGE_URL,
                        discountType = it.discountType,
                        storeType = StoreType.GS25,
                        referenceDate = now,
                    )
                )
            } catch (e: ItemDuplicatedException) {
                log.warn("Item duplicated", e)
            }
        }
    }

    companion object {
        private val log : Logger = LoggerFactory.getLogger(Gs25CollectorServiceImpl::class.java)
        const val NO_IMAGE_URL = "http://gs25.gsretail.com/_ui/desktop/common/images/gscvs/products/prd_no_img.gif"
    }

}