package com.haeseong.nplusone.domain.item

import java.lang.RuntimeException

class ItemNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)