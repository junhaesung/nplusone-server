package com.haeseong.nplusone.domain.scrapping

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface ScrappingResultRepository : JpaRepository<ScrappingResult, Long> {
    fun existsByReferenceDateAndNameAndStoreTypeAndDiscountType(
        referenceDate: LocalDate,
        name: String,
        storeType: StoreType,
        discountType: DiscountType,
    ): Boolean

    fun findByNameContainsAndDiscountTypeInAndStoreTypeInAndReferenceDateAndScrappingResultIdGreaterThan(
        name: String,
        discountTypes: Collection<DiscountType>,
        storeTypes: Collection<StoreType>,
        referenceDate: LocalDate = LocalDate.now(),
        offsetId: Long,
        pageable: Pageable = Pageable.ofSize(20),
    ): Slice<ScrappingResult>

    fun findByReferenceDateAndScrappingResultIdGreaterThanOrderByScrappingResultId(
        referenceDate: LocalDate,
        offsetScrappingResultId: Long,
        pageable: Pageable
    ): Slice<ScrappingResult>

    fun findByReferenceDateAndStoreTypeOrderByScrappingResultId(
        referenceDate: LocalDate,
        storeType: StoreType,
        pageable: Pageable,
    ): Slice<ScrappingResult>
}
