package com.haeseong.nplusone.domain.item.name

import com.haeseong.nplusone.domain.item.StoreType
import org.springframework.data.jpa.repository.JpaRepository

interface ItemNameRepository : JpaRepository<ItemName, Long> {
    fun existsByNameAndStoreType(name: String, storeType: StoreType): Boolean
    fun findByNameAndStoreType(name: String, storeType: StoreType): ItemName?
}