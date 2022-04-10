package com.haeseong.nplusone.domain

data class ItemQueryVo(
    val discountType: DiscountType?,
    val storeType: StoreType?,
    val offsetId: Long,
    val pageSize: Int
)
