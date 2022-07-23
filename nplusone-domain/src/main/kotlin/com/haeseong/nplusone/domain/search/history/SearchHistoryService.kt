package com.haeseong.nplusone.domain.search.history

import com.haeseong.nplusone.domain.member.MemberService
import com.haeseong.nplusone.domain.search.event.SearchEvent
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
        val member = memberService.getMember(memberId = searchEvent.memberId)
        val searchWord = searchEvent.searchWord
        val searchedAt = searchEvent.searchedAt

        // 이미 검색 기록이 존재하면, 검색 시간을 수정함
        val searchHistory = searchHistoryRepository.findByMemberAndSearchWord(member, searchWord)
        if (searchHistory != null) {
            searchHistory.searchedAt = searchedAt
            return SearchHistoryVo.from(searchHistory)
        }

        // 검색기록이 존재하지 않으면 새로 추가
        return searchHistoryRepository.save(
            SearchHistory(
                member = memberService.getMember(memberId = searchEvent.memberId),
                searchWord = searchEvent.searchWord,
                searchedAt = searchedAt,
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