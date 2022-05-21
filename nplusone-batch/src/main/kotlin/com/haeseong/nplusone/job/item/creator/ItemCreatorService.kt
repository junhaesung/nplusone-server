package com.haeseong.nplusone.job.item.creator

import com.haeseong.nplusone.domain.item.*
import com.haeseong.nplusone.domain.scrapping.ScrappingResultService
import com.haeseong.nplusone.domain.scrapping.ScrappingResultVo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Slice
import java.time.LocalDate

interface ItemCreatorService {
    fun createItems(referenceDate: LocalDate, storeType: StoreType? = null): List<ItemVo>
}

class ItemCreatorServiceImpl(
    private val scrappingResultService: ScrappingResultService,
    private val itemService: ItemService,
) : ItemCreatorService {
    override fun createItems(referenceDate: LocalDate, storeType: StoreType?): List<ItemVo> {
        var offsetScrappingResultId = 0L
        val createdItems = mutableListOf<ItemVo>()
        while (true) {
            val results = getScrappingResults(storeType, offsetScrappingResultId)
            if (results.isEmpty) {
                break
            }
            createdItems += createItems(scrappingResultVoList = results.content)
            offsetScrappingResultId = results.last().scrappingResultId
        }
        log.info("createdItems.size: ${createdItems.size}")
        return createdItems
    }

    private fun getScrappingResults(storeType: StoreType?, offsetScrappingResultId: Long): Slice<ScrappingResultVo> {
        return scrappingResultService.getItems(
            ItemQueryVo(
                name = null,
                discountType = null,
                storeType = storeType,
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