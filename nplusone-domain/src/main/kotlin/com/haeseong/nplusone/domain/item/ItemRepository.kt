package com.haeseong.nplusone.domain.item

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface ItemRepository : JpaRepository<Item, Long> {
    fun existsByReferenceDateAndNameAndStoreTypeAndDiscountType(
        referenceDate: LocalDate,
        name: String,
        storeType: StoreType,
        discountType: DiscountType,
    ): Boolean

    fun findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDateAndItemIdGreaterThan(
        name: String,
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        referenceDate: LocalDate = LocalDate.now(),
        offsetId: Long,
        pageable: Pageable = Pageable.ofSize(20),
    ): Slice<Item>
}