package com.haeseong.nplusone.ui.member

import com.haeseong.nplusone.application.LoginResponseVo
import com.haeseong.nplusone.application.MemberFacadeService
import com.haeseong.nplusone.domain.item.member.IdProvider
import com.haeseong.nplusone.domain.item.member.MemberVo
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberFacadeService: MemberFacadeService,
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequestDto: LoginRequestDto): ApiResponse<LoginResponseDto> {
        val loginResponseVo = memberFacadeService.login(
            idProvider = IdProvider(
                idProviderType = loginRequestDto.idProviderType,
                idProviderUserId = loginRequestDto.idProviderUserId,
            ),
        )
        return ApiResponse.success(
            data = toLoginResponseDto(loginResponseVo)
        )
    }

    private fun toLoginResponseDto(loginResponseVo: LoginResponseVo) = LoginResponseDto(
        member = toMemberResponseDto(memberVo = loginResponseVo.memberVo),
        accessToken = loginResponseVo.accessToken,
    )

    private fun toMemberResponseDto(memberVo: MemberVo) = MemberResponseDto(
        memberId = memberVo.memberId,
    )
}