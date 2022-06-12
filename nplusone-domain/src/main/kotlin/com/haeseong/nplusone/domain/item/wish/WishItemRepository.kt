package com.haeseong.nplusone.domain.item.wish

import org.springframework.data.jpa.repository.JpaRepository

interface WishItemRepository : JpaRepository<WishItem, Long> {
    @Suppress("FunctionName")
    fun existsByMember_memberIdAndItem_itemId(memberId: Long, itemId: Long): Boolean
    fun findByMember_memberIdAndItem_itemId(memberId: Long, itemId: Long): WishItem?
}
