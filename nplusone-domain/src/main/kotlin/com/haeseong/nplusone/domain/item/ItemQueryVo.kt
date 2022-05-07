package com.haeseong.nplusone.domain.item

data class ItemQueryVo(
    val name: String?,
    val discountType: DiscountType?,
    val storeType: StoreType?,
    val offsetId: Long,
    val pageSize: Int
)
