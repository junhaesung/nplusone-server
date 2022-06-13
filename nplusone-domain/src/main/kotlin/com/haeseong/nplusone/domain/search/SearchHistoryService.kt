package com.haeseong.nplusone.domain.search

import com.haeseong.nplusone.domain.member.MemberService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface SearchHistoryService {
    fun record(searchEvent: SearchEvent): SearchHistoryVo
    fun delete(memberId: Long, searchHistoryId: Long)
    fun getSearchHistories(memberId: Long): List<SearchHistoryVo>
}

@Service
@Transactional(readOnly = true)
class SearchHistoryServiceImpl(
    private val memberService: MemberService,
    private val searchHistoryRepository: SearchHistoryRepository,
) : SearchHistoryService {
    @Transactional
    override fun record(searchEvent: SearchEvent): SearchHistoryVo {
        return searchHistoryRepository.save(
            SearchHistory(
                member = memberService.getMember(memberId = searchEvent.memberId),
                searchWord = searchEvent.searchWord,
                searchedAt = searchEvent.searchedAt,
            )
        ).let { SearchHistoryVo.from(it) }
    }

    @Transactional
    override fun delete(memberId: Long, searchHistoryId: Long) {
        searchHistoryRepository.findByMember_memberIdAndSearchHistoryId(memberId, searchHistoryId)
            ?.run { searchHistoryRepository.delete(this) }
    }

    override fun getSearchHistories(memberId: Long): List<SearchHistoryVo> {
        return searchHistoryRepository.findByMember_memberId(
            memberId = memberId,
            pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "searchedAt")
        ).map { SearchHistoryVo.from(it) }
    }
}