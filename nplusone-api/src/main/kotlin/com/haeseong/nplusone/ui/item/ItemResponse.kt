package com.haeseong.nplusone.ui.item

import java.math.BigDecimal

data class ItemResponse(
    val itemId: Long,
    val name: String,
    val price: BigDecimal,
)
