package com.haeseong.nplusone.ui.member

import com.haeseong.nplusone.domain.item.member.IdProviderType

data class LoginRequestDto(
    val idProviderType: IdProviderType,
    val idProviderUserId: String,
)