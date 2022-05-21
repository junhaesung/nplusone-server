package com.haeseong.nplusone.domain.scrapping

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import java.math.BigDecimal
import java.time.LocalDate

data class ScrappingResultVo(
    val scrappingResultId: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val referenceDate: LocalDate,
) {
    companion object {
        fun from(scrappingResult: ScrappingResult) = ScrappingResultVo(
            scrappingResultId = scrappingResult.scrappingResultId,
            name = scrappingResult.name,
            price = scrappingResult.price,
            imageUrl = scrappingResult.imageUrl,
            discountType = scrappingResult.discountType,
            storeType = scrappingResult.storeType,
            referenceDate = scrappingResult.referenceDate,
        )
    }
}