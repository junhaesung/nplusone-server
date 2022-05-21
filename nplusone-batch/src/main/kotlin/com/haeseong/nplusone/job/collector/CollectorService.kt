package com.haeseong.nplusone.job.collector

interface CollectorService : DiscountedItemValidator {
    fun getDiscountedItems(): List<DiscountedItem>
    fun saveAll(discountedItems: List<DiscountedItem>)
}