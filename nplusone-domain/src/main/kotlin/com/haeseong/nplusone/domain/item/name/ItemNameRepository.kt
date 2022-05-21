package com.haeseong.nplusone.domain.item.name

import org.springframework.boot.autoconfigure.session.StoreType
import org.springframework.data.jpa.repository.JpaRepository

interface ItemNameRepository: JpaRepository<ItemName, Long> {
    fun existsByNameAndStoreType(name: String, storeType: StoreType): Boolean
}