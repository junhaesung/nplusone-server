package com.haeseong.nplusone.domain

import java.math.BigDecimal

data class ItemCreateVo(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val discountType: DiscountType,
)