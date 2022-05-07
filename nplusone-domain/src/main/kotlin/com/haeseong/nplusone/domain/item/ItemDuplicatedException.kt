package com.haeseong.nplusone.domain.item

class ItemDuplicatedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)