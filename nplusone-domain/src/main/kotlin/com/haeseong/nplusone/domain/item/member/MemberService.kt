package com.haeseong.nplusone.domain.item.member

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface MemberService {
    fun create(memberCreateVo: MemberCreateVo): MemberVo
    fun get(idProvider: IdProvider): MemberVo
    fun get(memberId: Long): MemberVo
}

@Service
@Transactional(readOnly = true)
class MemberServiceImpl(
    private val memberRepository: MemberRepository,
) : MemberService {

    @Transactional(readOnly = false)
    override fun create(memberCreateVo: MemberCreateVo): MemberVo {
        return memberRepository.save(
            Member(
                idProvider = memberCreateVo.idProvider,
                fcmToken = memberCreateVo.fcmToken,
            )
        ).let { MemberVo.from(it) }
    }

    override fun get(idProvider: IdProvider): MemberVo {
        val member = memberRepository.findByIdProvider(idProvider = idProvider) ?: throw MemberNotFoundException()
        return MemberVo.from(member = member)
    }

    override fun get(memberId: Long): MemberVo {
        return memberRepository.findByIdOrNull(memberId)
            ?.let { MemberVo.from(it) }
            ?: throw MemberNotFoundException()
    }

}