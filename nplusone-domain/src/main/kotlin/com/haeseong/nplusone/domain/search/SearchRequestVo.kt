package com.haeseong.nplusone.domain.search

import com.haeseong.nplusone.domain.item.DiscountType
import com.haeseong.nplusone.domain.item.StoreType

data class SearchRequestVo(
    val searchWord: String,
    val discountType: DiscountType?,
    val storeType: StoreType?,
    val offsetId: Long?,
    val pageSize: Int,
)
