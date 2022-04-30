package com.haeseong.nplusone.domain.item

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.time.YearMonth

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByDiscountTypeInAndStoreTypeInAndReferenceDateAndItemIdGreaterThan(
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        referenceDate: LocalDate = LocalDate.now(),
        offsetId: Long,
        pageable: Pageable = Pageable.ofSize(20)
    ): Slice<Item>
}