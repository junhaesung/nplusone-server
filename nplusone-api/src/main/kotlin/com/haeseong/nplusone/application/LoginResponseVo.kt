package com.haeseong.nplusone.application

import com.haeseong.nplusone.domain.member.MemberVo

data class LoginResponseVo(
    val memberVo: MemberVo,
    val accessToken: String,
)