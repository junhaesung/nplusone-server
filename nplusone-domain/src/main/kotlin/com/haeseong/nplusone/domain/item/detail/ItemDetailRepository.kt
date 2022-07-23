package com.haeseong.nplusone.domain.item.detail

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface ItemDetailRepository : JpaRepository<ItemDetail, Long> {
    fun existsByNameAndDiscountTypeAndStoreTypeAndReferenceDate(
        name: String,
        discountType: DiscountType,
        storeType: StoreType,
        referenceDate: LocalDate,
    ): Boolean

    fun findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDateAndItemDetailIdGreaterThan(
        name: String,
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        referenceDate: LocalDate,
        offsetId: Long,
        pageable: Pageable = PageRequest.of(0, 20, Sort.Direction.ASC, "itemDetailId"),
    ): Slice<ItemDetail>

    fun findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDate(
        name: String,
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        referenceDate: LocalDate,
        pageable: Pageable,
    ): Page<ItemDetail>

    fun findByStoreTypeAndReferenceDate(
        storeType: StoreType,
        referenceDate: LocalDate,
        pageable: Pageable,
    ): Slice<ItemDetail>

    fun findByDiscountTypeAndReferenceDate(
        discountType: DiscountType,
        referenceDate: LocalDate,
        pageable: Pageable,
    ): Slice<ItemDetail>
}