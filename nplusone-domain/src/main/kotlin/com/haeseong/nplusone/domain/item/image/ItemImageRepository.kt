package com.haeseong.nplusone.domain.item.image

import com.haeseong.nplusone.domain.item.StoreType
import org.springframework.data.jpa.repository.JpaRepository

interface ItemImageRepository : JpaRepository<ItemImage, Long> {
    fun existsByImageUrlAndStoreType(imageUrl: String?, storeType: StoreType): Boolean
}