package com.haeseong.nplusone.domain

import java.math.BigDecimal

data class DiscountedItem(
        val name: String?,
        val price: BigDecimal?,
        val imageUrl: String?,
        val discountType: DiscountType,
) {
    fun validate() {
        assert(!name.isNullOrBlank()) { "'name' is must not blank" }
        assert(price != null) { "'price' must not be null" }
        assert(price!! >= BigDecimal.ZERO) { "'price' must be greater than or equal to zero" }
        assert(imageUrl != null) { "'imageUrl' must not be null" }
    }
}