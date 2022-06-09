package com.haeseong.nplusone.domain.member

data class MemberVo(
    val memberId: Long,
    val idProvider: IdProvider,
    val fcmToken: String,
) {
    companion object {
        fun from(member: Member) = MemberVo(
            memberId = member.memberId,
            idProvider = member.idProvider,
            fcmToken = member.fcmToken,
        )
    }
}