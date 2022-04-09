package com.haeseong.nplusone

enum class DiscountType {
    ONE_PLUS_ONE,
    TWO_PLUS_ONE,
    UNKNOWN,
    ;

    companion object {
        fun parse(text: String): DiscountType = when (text) {
            "1+1" -> ONE_PLUS_ONE
            "2+1" -> TWO_PLUS_ONE
            else -> UNKNOWN
        }
    }
}