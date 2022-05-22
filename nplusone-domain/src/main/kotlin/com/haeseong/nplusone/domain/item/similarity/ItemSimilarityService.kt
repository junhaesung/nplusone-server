package com.haeseong.nplusone.domain.item.similarity

import com.haeseong.nplusone.domain.item.Item
import com.haeseong.nplusone.domain.item.ItemNotFoundException
import com.haeseong.nplusone.domain.item.ItemRepository
import com.haeseong.nplusone.domain.item.ItemService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface ItemSimilarityService {
    fun createOrUpdate(sourceItemId: Long, targetItemId: Long): ItemSimilarity
    fun createOrUpdate(sourceItem: Item, targetItem: Item): ItemSimilarity
}

@Service
class ItemSimilarityServiceImpl(
    private val itemService: ItemService,
    private val itemSimilarityRepository: ItemSimilarityRepository,
    private val similarityCalculator: SimilarityCalculator,
) : ItemSimilarityService {

    @Transactional
    override fun createOrUpdate(sourceItemId: Long, targetItemId: Long): ItemSimilarity {
        val sourceItem = itemService.getItem(itemId = sourceItemId)
        val targetItem = itemService.getItem(itemId = targetItemId)
        return createOrUpdateItemSimilarity(
            sourceItem = sourceItem,
            targetItem = targetItem,
        )
    }

    @Transactional
    override fun createOrUpdate(sourceItem: Item, targetItem: Item): ItemSimilarity {
        return createOrUpdateItemSimilarity(
            sourceItem = sourceItem,
            targetItem = targetItem,
        )
    }

    private fun createOrUpdateItemSimilarity(sourceItem: Item, targetItem: Item): ItemSimilarity {
        itemSimilarityRepository.findBySourceItemAndTargetItem(
            sourceItem = sourceItem,
            targetItem = targetItem,
        )?.let {
            it.calculate(similarityCalculator)
            return it
        }
        return itemSimilarityRepository.save(
            ItemSimilarity.of(
                sourceItem = sourceItem,
                targetItem = targetItem,
                similarityCalculator = similarityCalculator,
            )
        )
    }
}