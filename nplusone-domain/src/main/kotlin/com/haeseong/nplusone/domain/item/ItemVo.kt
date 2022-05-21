package com.haeseong.nplusone.domain.item

import java.math.BigDecimal

data class ItemVo(
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
) {
    companion object {
        fun from(item: Item) = ItemVo(
            itemId = item.itemId,
            name = item.name,
            price = item.price,
        )
    }
}
