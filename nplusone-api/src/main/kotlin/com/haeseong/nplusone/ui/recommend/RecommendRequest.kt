package com.haeseong.nplusone.ui.recommend

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType

data class RecommendRequest (
    val storeType: StoreType?,
    val discountType: DiscountType?,
)
