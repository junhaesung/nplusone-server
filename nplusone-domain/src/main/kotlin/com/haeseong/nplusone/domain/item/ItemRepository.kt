package com.haeseong.nplusone.domain.item

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByName(name: String): Item?
    fun findByItemIdGreaterThanOrderByItemIdAsc(offsetItemId: Long, pageable: Pageable): List<ItemNameVo>
    fun findByItemIdIn(itemIds: Collection<Long>): List<Item>
}