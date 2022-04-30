package com.haeseong.nplusone.domain.item

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class ItemCreateVo(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val referenceDate: LocalDate,
)