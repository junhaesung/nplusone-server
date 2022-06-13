package com.haeseong.nplusone.ui

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

fun Authentication.resolveMemberId(): Long {
    if (this is PreAuthenticatedAuthenticationToken) {
        return this.name.toLong()
    }
    throw IllegalArgumentException()
}
