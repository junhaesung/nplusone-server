package com.haeseong.nplusone.job.item

import com.haeseong.nplusone.domain.item.ItemCreateVo
import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.ItemVo
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.domain.scrapping.ScrappingResultVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Slice
import java.time.LocalDate

interface ItemCreatorService {
    fun createItems(referenceDate: LocalDate): List<ItemVo>
}

class ItemCreatorServiceImpl(
    private val scrappingResultService: ScrappingResultService,
    private val itemService: ItemService,
) : ItemCreatorService {
    override fun createItems(referenceDate: LocalDate): List<ItemVo> {
        var offsetScrappingResultId = 0L
        val createdItems = mutableListOf<ItemVo>()
        while (true) {
            val results = getScrappingResults(offsetScrappingResultId)
            if (results.isEmpty) {
                break
            }
            createdItems += createItems(scrappingResultVoList = results.content)
            offsetScrappingResultId = results.last().scrappingResultId
        }
        log.info("createdItems.size: ${createdItems.size}")
        return createdItems
    }

    private fun getScrappingResults(offsetScrappingResultId: Long): Slice<ScrappingResultVo> {
        return scrappingResultService.getItems(
            ItemQueryVo(
                name = null,
                discountType = null,
                storeType = null,
                offsetId = offsetScrappingResultId,
                pageSize = 100
            )
        )
    }

    private fun createItems(scrappingResultVoList: List<ScrappingResultVo>): List<ItemVo> {
        return scrappingResultVoList.map {
            ItemCreateVo(
                name = it.name,
                price = it.price,
                imageUrl = it.imageUrl,
                storeType = it.storeType,
            )
        }.map {
            itemService.create(itemCreateVo = it)
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemCreatorServiceImpl::class.java)
    }
}