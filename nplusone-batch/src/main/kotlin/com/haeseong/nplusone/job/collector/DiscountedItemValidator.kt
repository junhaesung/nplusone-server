package com.haeseong.nplusone.job.collector

import org.slf4j.Logger
import org.slf4j.LoggerFactory

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