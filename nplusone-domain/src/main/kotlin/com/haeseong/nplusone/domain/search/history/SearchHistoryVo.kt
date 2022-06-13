package com.haeseong.nplusone.domain.search.history

import com.haeseong.nplusone.domain.member.MemberVo
import java.time.LocalDateTime

data class SearchHistoryVo(
    val searchHistoryId: Long,
    val memberVo: MemberVo,
    val searchWord: String,
    val searchedAt: LocalDateTime,
) {
    companion object {
        fun from(searchHistory: SearchHistory) = SearchHistoryVo(
            searchHistoryId = searchHistory.searchHistoryId,
            memberVo = MemberVo.from(searchHistory.member),
            searchWord = searchHistory.searchWord,
            searchedAt = searchHistory.searchedAt,
        )
    }
}