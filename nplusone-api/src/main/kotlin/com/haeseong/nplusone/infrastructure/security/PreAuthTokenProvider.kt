package com.haeseong.nplusone.infrastructure.security

import com.haeseong.nplusone.application.TokenService
import com.haeseong.nplusone.domain.item.member.MemberService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

class PreAuthTokenProvider(
    private val memberService: MemberService,
    private val tokenService: TokenService<Long>,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        if (authentication is PreAuthenticatedAuthenticationToken) {
            val token = authentication.getPrincipal().toString()
            val applicantId: Long = tokenService.decode(token)
            val memberVo = try {
                memberService.get(applicantId)
            } catch (e: Exception) {
                log.warn("member not found", e)
                throw TokenMissingException("Member not found")
            }
            return PreAuthenticatedAuthenticationToken(
                memberVo.memberId,
                "",
                listOf(SimpleGrantedAuthority(SecurityConfig.MEMBER_ROLE_NAME))
            )
        }
        throw TokenMissingException("Invalid token")
    }

    override fun supports(authentication: Class<*>): Boolean {
        return PreAuthenticatedAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PreAuthTokenProvider::class.java)
    }
}
