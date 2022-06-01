package com.haeseong.nplusone.domain.item.member

import java.lang.RuntimeException

class MemberNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)