package com.haeseong.nplusone.domain.item.detail

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface ItemDetailRepository : JpaRepository<ItemDetail, Long> {
    fun existsByNameAndDiscountTypeAndStoreTypeAndReferenceDate(
        name: String,
        discountType: DiscountType,
        storeType: StoreType,
        referenceDate: LocalDate,
    ): Boolean
}