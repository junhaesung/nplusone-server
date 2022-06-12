package com.haeseong.nplusone.domain.item.wish

import com.haeseong.nplusone.domain.item.ItemNotFoundException
import com.haeseong.nplusone.domain.item.ItemRepository
import com.haeseong.nplusone.domain.member.MemberNotFoundException
import com.haeseong.nplusone.domain.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface WishItemService {
    fun add(memberId: Long, itemId: Long)
    fun remove(memberId: Long, itemId: Long)
}

@Service
@Transactional(readOnly = true)
class WishItemServiceImpl(
    private val wishItemRepository: WishItemRepository,
    private val memberRepository: MemberRepository,
    private val itemRepository: ItemRepository,
) : WishItemService {

    @Transactional
    override fun add(memberId: Long, itemId: Long) {
        val alreadyExists = wishItemRepository.existsByMember_memberIdAndItem_itemId(
            memberId = memberId,
            itemId = itemId,
        )
        if (alreadyExists) {
            return
        }
        val member = memberRepository.findByIdOrNull(memberId) ?: throw MemberNotFoundException()
        val item = itemRepository.findByIdOrNull(itemId) ?: throw ItemNotFoundException()
        wishItemRepository.save(
            WishItem(
                member = member,
                item = item,
            )
        )
    }

    @Transactional
    override fun remove(memberId: Long, itemId: Long) {
        wishItemRepository.findByMember_memberIdAndItem_itemId(
            memberId = memberId,
            itemId = itemId,
        )?.let { wishItemRepository.delete(it) }
    }

}
