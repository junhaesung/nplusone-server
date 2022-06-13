package com.haeseong.nplusone.domain.item

data class ItemQueryVo(
    val name: String?,
    val discountType: DiscountType? = null,
    val storeType: StoreType? = null,
    val offsetId: Long,
    val pageSize: Int
)
