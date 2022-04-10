package com.haeseong.nplusone.ui

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import java.math.BigDecimal
import java.time.YearMonth

data class ItemResponse(
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val yearMonth: YearMonth,
) : OffsetIdSupport {
    override val offsetId: Long
        get() = itemId
}
