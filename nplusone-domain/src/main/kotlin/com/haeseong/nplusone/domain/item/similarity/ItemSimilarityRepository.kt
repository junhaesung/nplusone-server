package com.haeseong.nplusone.domain.item.similarity

import com.haeseong.nplusone.domain.item.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemSimilarityRepository : JpaRepository<ItemSimilarity, Long> {
    fun findBySourceItemAndTargetItem(sourceItem: Item, targetItem: Item): ItemSimilarity?
}