package com.haeseong.nplusone.domain.search.summary

import java.time.LocalDateTime

data class SearchSummaryVo(
    val searchSummaryId: Long,
    val searchWord: String,
    val referenceDateTime: LocalDateTime,
    val searchCount: Int,
    val rank: Int,
) {
    companion object {
        fun from(searchSummary: SearchSummary) = SearchSummaryVo(
            searchSummaryId = searchSummary.searchSummaryId,
            searchWord = searchSummary.searchWord,
            referenceDateTime = searchSummary.referenceDateTime,
            searchCount = searchSummary.searchCount,
            rank = searchSummary.searchRank,
        )
    }
}
