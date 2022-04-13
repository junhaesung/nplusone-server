package com.haeseong.nplusone.job.collector

import com.haeseong.nplusone.domain.item.DiscountedItem
import com.haeseong.nplusone.job.collector.cu.CuCollectorTasklet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

interface DiscountedItemValidator {
    fun validateAll(discountedItems: List<DiscountedItem>) {
        val invalidItems = discountedItems.filter {
            try {
                it.validate()
                false
            } catch (e: Exception) {
                log.error("Invalid item", e)
                true
            }
        }
        log.info("Number of invalid items: ${invalidItems.size}")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(DiscountedItemValidator::class.java)
    }
}