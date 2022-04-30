package com.haeseong.nplusone.domain.item

import java.math.BigDecimal
import java.time.LocalDate

data class ItemVo(
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val referenceDate: LocalDate,
) {
    companion object {
        fun from(item: Item): ItemVo = ItemVo(
            itemId = item.itemId,
            name = item.name,
            price = item.price,
            imageUrl = item.imageUrl,
            discountType = item.discountType,
            storeType = item.storeType,
            referenceDate = item.referenceDate,
        )
    }
}