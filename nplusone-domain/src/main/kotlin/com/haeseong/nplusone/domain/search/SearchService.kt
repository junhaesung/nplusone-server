package com.haeseong.nplusone.domain.search

import com.haeseong.nplusone.domain.item.ItemQueryVo
import com.haeseong.nplusone.domain.item.detail.ItemDetailService
import com.haeseong.nplusone.domain.item.detail.ItemDetailVo
import com.haeseong.nplusone.domain.search.event.SearchEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface SearchService {
    fun search(memberId: Long, searchRequestVo: SearchRequestVo): Page<ItemDetailVo>
}

@Service
@Transactional(readOnly = true)
class SearchServiceImpl(
    private val itemDetailService: ItemDetailService,
    private val searchEventPublisher: SearchEventPublisher,
) : SearchService {

    override fun search(memberId: Long, searchRequestVo: SearchRequestVo): Page<ItemDetailVo> {
        publishSearchEvent(memberId, searchRequestVo)

        // 처음 요청하면 offsetId is null
        if (searchRequestVo.offsetId == null) {
            return itemDetailService.getItemDetailPage(
                itemQueryVo = ItemQueryVo(
                    name = searchRequestVo.searchWord,
                    offsetId = 0L,
                    pageSize = searchRequestVo.pageSize,
                )
            )
        }
        return PageImpl(
            itemDetailService.getItemDetails(
                itemQueryVo = ItemQueryVo(
                    name = searchRequestVo.searchWord,
                    offsetId = searchRequestVo.offsetId,
                    pageSize = searchRequestVo.pageSize,
                )
            ).content
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