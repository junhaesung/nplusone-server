package com.haeseong.nplusone.domain.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByIdProvider(idProvider: IdProvider): Member?
}