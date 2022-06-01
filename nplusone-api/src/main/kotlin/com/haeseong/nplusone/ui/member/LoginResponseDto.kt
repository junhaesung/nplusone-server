package com.haeseong.nplusone.ui.member

data class LoginResponseDto(
    val member: MemberResponseDto,
    val accessToken: String,
)