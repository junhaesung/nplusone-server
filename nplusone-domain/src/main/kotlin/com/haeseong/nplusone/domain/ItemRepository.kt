package com.haeseong.nplusone.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {
    fun findByDiscountTypeInAndStoreTypeInAndItemIdGreaterThan(
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        offsetId: Long,
        pageable: Pageable = Pageable.ofSize(20)
    ): Slice<Item>
}