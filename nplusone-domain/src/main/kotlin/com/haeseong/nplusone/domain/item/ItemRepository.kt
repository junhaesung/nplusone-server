package com.haeseong.nplusone.domain.item

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.YearMonth

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByDiscountTypeInAndStoreTypeInAndYearMonthAndItemIdGreaterThan(
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        yearMonth: YearMonth = YearMonth.now(),
        offsetId: Long,
        pageable: Pageable = Pageable.ofSize(20)
    ): Slice<Item>
}