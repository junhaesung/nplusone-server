package com.haeseong.nplusone.job.collector

import com.haeseong.nplusone.domain.item.DiscountType
import java.math.BigDecimal

data class DiscountedItem(
    val name: String?,
    val price: BigDecimal?,
    val imageUrl: String?,
    val discountType: DiscountType,
) {
    fun validate() {
        require(!name.isNullOrBlank()) { "'name' is must not blank" }
        require(price != null) { "'price' must not be null" }
        require(price >= BigDecimal.ZERO) { "'price' must be greater than or equal to zero" }
    }
}