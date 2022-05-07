package com.haeseong.nplusone.ui.item

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType

data class ItemQueryRequest(
    val name: String?,
    val discountType: DiscountType?,
    val storeType: StoreType?,
    val offsetId: Long = 0,
    val pageSize: Int = 20,
)