package com.haeseong.nplusone.domain.item.detail

import java.lang.RuntimeException

class ItemDetailDuplicatedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)