package com.haeseong.nplusone.infrastructure.jwt

import com.haeseong.nplusone.application.TokenService
import org.springframework.stereotype.Service

@Service
class JwtService : TokenService<Long> {
    override fun encode(memberId: Long): String {
        return ""
    }

    override fun decode(token: String): Long {
        return 0L
    }
}