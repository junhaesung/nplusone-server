package com.haeseong.nplusone.infrastructure.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.haeseong.nplusone.application.TokenService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,
) : TokenService<Long> {

    private val algorithm: Algorithm = Algorithm.HMAC256(secretKey)
    private var jwtVerifier: JWTVerifier = JWT.require(algorithm).build()

    override fun encode(memberId: Long): String {
        return JWT.create()
            .withClaim(CLAIM_NAME_MEMBER_ID, memberId)
            .sign(algorithm)
    }

    override fun decode(token: String): Long {
        return try {
            jwtVerifier.verify(token).getClaim(CLAIM_NAME_MEMBER_ID).asLong()
        } catch (e: JWTVerificationException) {
            log.warn("Failed to decode jwt. token: {}", token, e)
            throw e
        }
    }

    companion object {
        private const val CLAIM_NAME_MEMBER_ID = "memberId"
        private val log: Logger = LoggerFactory.getLogger(JwtService::class.java)
    }
}