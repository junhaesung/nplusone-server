package com.haeseong.nplusone.domain.search

import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.detail.ItemDetailService
import com.haeseong.nplusone.domain.item.detail.ItemDetailVo
import com.haeseong.nplusone.domain.search.event.SearchEventPublisher
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface SearchService {
    fun search(memberId: Long, searchRequestVo: SearchRequestVo): Slice<ItemDetailVo>
    fun count(searchRequestVo: SearchRequestVo): Long
}

@Service
@Transactional(readOnly = true)
class SearchServiceImpl(
    private val itemDetailService: ItemDetailService,
    private val searchEventPublisher: SearchEventPublisher,
) : SearchService {

    override fun search(memberId: Long, searchRequestVo: SearchRequestVo): Slice<ItemDetailVo> {
        publishSearchEvent(memberId, searchRequestVo)

        return itemDetailService.getItemDetails(
            itemQueryVo = ItemQueryVo(
                name = searchRequestVo.searchWord,
                discountType = searchRequestVo.discountType,
                storeType = searchRequestVo.storeType,
                offsetId = searchRequestVo.offsetId,
                pageSize = searchRequestVo.pageSize,
            )
        )
    }

    override fun count(searchRequestVo: SearchRequestVo): Long {
        return itemDetailService.countItems(
            itemQueryVo = ItemQueryVo(
                name = searchRequestVo.searchWord,
                discountType = searchRequestVo.discountType,
                storeType = searchRequestVo.storeType,
                offsetId = searchRequestVo.offsetId,
                pageSize = searchRequestVo.pageSize,
            )
        )
    }

    private fun publishSearchEvent(memberId: Long, searchRequestVo: SearchRequestVo) {
        searchEventPublisher.publish(
            memberId = memberId,
            searchWord = searchRequestVo.searchWord,
            searchedAt = LocalDateTime.now(),
        )
    }

}