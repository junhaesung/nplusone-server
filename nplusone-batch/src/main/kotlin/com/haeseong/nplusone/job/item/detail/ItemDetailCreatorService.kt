package com.haeseong.nplusone.job.item.detail

import com.haeseong.nplusone.domain.item.detail.ItemDetailDuplicatedException
import com.haeseong.nplusone.domain.item.detail.ItemDetailService
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.domain.scrapping.ScrappingResultVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate

interface ItemDetailCreatorService {
    fun create(referenceDate: LocalDate)
}

class ItemDetailCreatorServiceImpl(
    private val scrappingResultService: ScrappingResultService,
    private val itemDetailService: ItemDetailService,
) : ItemDetailCreatorService {
    override fun create(referenceDate: LocalDate) {
        var offsetScrappingResultId = 0L
        while (true) {
            val results = scrappingResultService.getScrappingResults(
                referenceDate = referenceDate,
                offsetScrappingResultId = offsetScrappingResultId,
                limit = 1000,
            )
            if (results.isEmpty) {
                break
            }
            offsetScrappingResultId = results.last().scrappingResultId
            createItemDetails(scrappingResultVos = results.content)
        }
    }

    private fun createItemDetails(scrappingResultVos: Collection<ScrappingResultVo>) {
        scrappingResultVos.forEach {
            try {
                itemDetailService.create(scrappingResultVo = it)
            } catch (e: ItemDetailDuplicatedException) {
                log.warn("itemDetail already exists.", e)
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemDetailCreatorServiceImpl::class.java)
    }
}