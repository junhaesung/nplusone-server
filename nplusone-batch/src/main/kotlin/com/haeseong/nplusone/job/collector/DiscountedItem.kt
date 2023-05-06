package com.haeseong.nplusone.job.collector

import com.haeseong.nplusone.domain.item.DiscountType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal

data class DiscountedItem(
    val name: String?,
    val price: BigDecimal?,
    val imageUrl: String?,
    val discountType: DiscountType,
    val referenceUrl: String? = null,
) {
    fun validate() {
        require(!name.isNullOrBlank()) { "'name' is must not blank" }
        require(price != null) { "'price' must not be null" }
        require(price >= BigDecimal.ZERO) { "'price' must be greater than or equal to zero" }
        if (price.compareTo(BigDecimal.ZERO) == 0)
        log.warn("'price' is zero. item: $this")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(DiscountedItem::class.java)
    }
}
