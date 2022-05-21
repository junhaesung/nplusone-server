package com.haeseong.nplusone.domain.item

import java.math.BigDecimal

data class ItemCreateVo(
    val name: String,
    val price: BigDecimal,
    val imageUrl: String?,
    val storeType: StoreType,
)