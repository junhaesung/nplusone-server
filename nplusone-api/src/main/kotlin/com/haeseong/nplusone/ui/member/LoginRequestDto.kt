package com.haeseong.nplusone.ui.member

import com.haeseong.nplusone.domain.member.IdProviderType

data class LoginRequestDto(
    val idProviderType: IdProviderType,
    val idProviderUserId: String,
)