package com.haeseong.nplusone.domain.member

data class MemberCreateVo(
    val idProvider: IdProvider,
    val fcmToken: String,
)
