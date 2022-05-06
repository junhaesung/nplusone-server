package com.haeseong.nplusone.ui.item

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import com.haeseong.nplusone.ui.OffsetIdSupport
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class ItemResponse(
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val referenceDate: LocalDate,
) : OffsetIdSupport {
    @get:JsonIgnore
    override val offsetId: Long
        get() = itemId
}
