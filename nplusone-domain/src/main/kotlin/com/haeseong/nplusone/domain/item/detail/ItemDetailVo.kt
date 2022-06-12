package com.haeseong.nplusone.domain.item.detail

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType
import java.math.BigDecimal
import java.time.LocalDate

data class ItemDetailVo(
    val itemDetailId: Long,
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
    val referenceDate: LocalDate,
) {
    companion object {
        fun from(itemDetail: ItemDetail) = ItemDetailVo(
            itemDetailId = itemDetail.itemDetailId,
            itemId = itemDetail.item.itemId,
            name = itemDetail.name,
            price = itemDetail.price,
            imageUrl = itemDetail.imageUrl,
            discountType = itemDetail.discountType,
            storeType = itemDetail.storeType,
            referenceDate = itemDetail.referenceDate,
        )
    }
}