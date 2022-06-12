package com.haeseong.nplusone.domain.item.wish

import org.springframework.data.jpa.repository.JpaRepository

@Suppress("FunctionName")
interface WishItemRepository : JpaRepository<WishItem, Long> {
    fun existsByMember_memberIdAndItem_itemId(memberId: Long, itemId: Long): Boolean
    fun findByMember_memberIdAndItem_itemId(memberId: Long, itemId: Long): WishItem?
    fun findByMember_memberId(memberId: Long): List<WishItem>
}
