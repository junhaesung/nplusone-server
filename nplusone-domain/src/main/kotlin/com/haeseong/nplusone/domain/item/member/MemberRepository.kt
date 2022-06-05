package com.haeseong.nplusone.domain.item.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByIdProvider(idProvider: IdProvider): Member?
}