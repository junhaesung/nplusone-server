package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.ItemVo
import com.haeseong.nplusone.domain.item.detail.ItemDetailVo

fun ItemVo.toDto() = ItemVo(
    itemId = this.itemId,
    name = this.name,
    price = this.price,
)

fun ItemDetailVo.toDto() = ItemDetailResponse(
    itemDetailId = this.itemDetailId,
    itemId = this.itemId,
    name = this.name,
    price = this.price,
    imageUrl = this.imageUrl,
    discountType = this.discountType,
    storeType = this.storeType,
    referenceDate = this.referenceDate,
)