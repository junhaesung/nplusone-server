package com.haeseong.nplusone.job.collector.cu

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

class CuCollectorService(
    private val scrappingResultService: ScrappingResultService,
) : CollectorService {
    override fun getDiscountedItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val chromeOptions = ChromeOptions()
        chromeOptions.setHeadless(true)
        chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage")
        val driver = ChromeDriver(chromeOptions)
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            driver.get("https://cu.bgfretail.com/event/plus.do")
            goToLastPage(driver)
            return driver.findElementsByCssSelector("#contents > div.relCon > div > ul > li")
                .map {
                    DiscountedItem(
                        name = it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_text > div.name")).text,
                        price = it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_text > div.price")).text.replace(
                            Regex("[,원\\s]"),
                            "").toBigDecimal(),
                        imageUrl = it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_img > img"))
                            .getAttribute("src"),
                        discountType = DiscountType.parse(it.findElement(By.cssSelector("a > div.badge > span")).text.trim()),
                    )
                }
        } finally {
            driver.quit()
        }
    }

    private fun goToLastPage(driver: ChromeDriver) {
        while (true) {
            val seeMoreButton = try {
                driver.findElementByCssSelector("#contents > div.relCon > div > div > div.prodListBtn-w > a")
            } catch (e: NoSuchElementException) {
                log.info("더 보기 버튼이 없음")
                break
            }

            try {
                seeMoreButton.click()
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
                        storeType = StoreType.CU,
                        referenceDate = now,
                    )
                )
            } catch (e: ScrappingResultDuplicatedException) {
                log.warn("scrappingResult is duplicated", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CuCollectorService::class.java)
    }
}