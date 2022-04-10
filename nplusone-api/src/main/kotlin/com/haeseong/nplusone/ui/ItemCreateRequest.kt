package com.haeseong.nplusone.ui

import com.haeseong.nplusone.domain.DiscountType
import com.haeseong.nplusone.domain.StoreType
import java.math.BigDecimal

data class ItemCreateRequest(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
    val storeType: StoreType,
)