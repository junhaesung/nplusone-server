package com.haeseong.nplusone.domain

import java.math.BigDecimal

data class ItemVo(
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
) {
    companion object {
        fun from(item: Item): ItemVo = ItemVo(
            itemId = item.itemId,
            name = item.name,
            price = item.price,
            imageUrl = item.imageUrl,
            discountType = item.discountType,
        )
    }
}