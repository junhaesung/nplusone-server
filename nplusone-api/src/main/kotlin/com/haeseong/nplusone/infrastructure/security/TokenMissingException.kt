package com.haeseong.nplusone.infrastructure.security

import org.springframework.security.core.userdetails.UsernameNotFoundException

class TokenMissingException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : UsernameNotFoundException(message, cause)
