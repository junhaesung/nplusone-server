package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.ItemVo

fun ItemVo.toDto() = ItemVo(
    itemId = this.itemId,
    name = this.name,
    price = this.price,
)