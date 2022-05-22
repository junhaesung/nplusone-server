package com.haeseong.nplusone.job.item.similarity

import com.haeseong.nplusone.domain.item.ItemService
import com.haeseong.nplusone.domain.item.similarity.ItemSimilarityService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

interface ItemSimilarityFacadeService {
    fun calculateAllSimilarity()
}

class ItemSimilarityFacadeServiceImpl(
    private val itemService: ItemService,
    private val itemSimilarityService: ItemSimilarityService,
) : ItemSimilarityFacadeService {

    // FIXME: 성능 개선 필요. local 환경에서 10시간 소요
    override fun calculateAllSimilarity() {
        val itemNames = itemService.getAllItemNames().toTypedArray()
        var k = 0
        val number = (itemNames.size - 1) * (itemNames.size - 1) / 2
        for (i in itemNames.indices) {
            for (j in i + 1 until itemNames.size) {
                if (++k % 10000 == 0) {
                    log.info("completed: $k / $number")
                }
                itemSimilarityService.createOrUpdate(
                    sourceItemId = itemNames[i].itemId,
                    targetItemId = itemNames[j].itemId,
                )
            }
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ItemSimilarityFacadeServiceImpl::class.java)
    }
}