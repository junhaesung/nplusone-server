package com.haeseong.nplusone.ui

import java.lang.RuntimeException

class BadRequestException(
    override val message: String? = null,
    override val cause: Throwable? = null,
): RuntimeException(message, cause)