package com.haeseong.nplusone.job.collector.cu

import com.haeseong.nplusone.domain.item.*
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.By
import org.openqa.selenium.InvalidElementStateException
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.chrome.ChromeDriver
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

open class CuCollectorTasklet : Tasklet {
    @Autowired
    lateinit var itemService: ItemService

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val discountedItems = getDiscountedItems()
        log.info("discountedItems.size: ${discountedItems.size}")
        validateAll(discountedItems)
        saveAll(discountedItems)
        return RepeatStatus.FINISHED
    }

    private fun getDiscountedItems(): List<DiscountedItem> {
        WebDriverManager.chromedriver().setup()
        val driver = ChromeDriver()
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS)

        try {
            driver.get("https://cu.bgfretail.com/event/plus.do")
            goToLastPage(driver)
            return driver.findElementsByCssSelector("#contents > div.relCon > div > ul > li")
                .map {
                    DiscountedItem(
                        it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_text > div.name")).text,
                        it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_text > div.price")).text.replace(Regex("[,원\\s]"), "").toBigDecimal(),
                        it.findElement(By.cssSelector("a > div.prod_wrap > div.prod_img > img")).getAttribute("src"),
                        DiscountType.parse(it.findElement(By.cssSelector("a > div.badge > span")).text.trim()),
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
                println("더 보기 버튼이 없음")
                break
            }

            try {
                seeMoreButton.click()
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

    private fun validateAll(discountedItems: List<DiscountedItem>) {
        val invalidItems = discountedItems.filter {
            try {
                it.validate()
                false
            } catch (e: Exception) {
                log.error("Invalid item", e)
                true
            }
        }
        log.info("Number of invalid items: ${invalidItems.size}")
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
                    storeType = StoreType.CU,
                    yearMonth = now,
                )
            )
        }
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(CuCollectorTasklet::class.java)
    }
}