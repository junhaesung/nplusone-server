package com.haeseong.nplusone.application

import com.haeseong.nplusone.domain.member.IdProvider
import com.haeseong.nplusone.domain.member.MemberCreateVo
import com.haeseong.nplusone.domain.member.MemberNotFoundException
import com.haeseong.nplusone.domain.member.MemberService
import org.springframework.stereotype.Service

interface MemberFacadeService {
    fun login(idProvider: IdProvider): LoginResponseVo
}

@Service
class MemberFacadeServiceImpl(
    private val memberService: MemberService,
    private val tokenService: TokenService<Long>,
): MemberFacadeService {
    override fun login(idProvider: IdProvider): LoginResponseVo {
        val member = try {
            memberService.get(idProvider = idProvider)
        } catch (e: MemberNotFoundException) {
            memberService.create(memberCreateVo = MemberCreateVo(
                idProvider = idProvider,
                fcmToken = "",
            ))
        }
        val token = tokenService.encode(memberId = member.memberId)
        return LoginResponseVo(
            memberVo = member,
            accessToken = token,
        )
    }
}