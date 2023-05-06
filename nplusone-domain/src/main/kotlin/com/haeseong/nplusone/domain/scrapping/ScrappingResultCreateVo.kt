package com.haeseong.nplusone.domain.scrapping

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import java.math.BigDecimal
import java.time.LocalDate

data class ScrappingResultCreateVo(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val referenceDate: LocalDate,
    val referenceUrl: String? = null,
)
