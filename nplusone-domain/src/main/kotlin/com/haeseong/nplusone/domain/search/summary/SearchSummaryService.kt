package com.haeseong.nplusone.domain.search.summary

import com.haeseong.nplusone.domain.search.history.SearchHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

interface SearchSummaryService {
    fun create(referenceDateTime: LocalDateTime): List<SearchSummaryVo>
    fun getSearchSummaries(requestDateTime: LocalDateTime): List<SearchSummaryVo>
}

@Service
@Transactional(readOnly = true)
class SearchSummaryServiceImpl(
    private val searchSummaryRepository: SearchSummaryRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) : SearchSummaryService {
    @Transactional
    override fun create(referenceDateTime: LocalDateTime): List<SearchSummaryVo> {
        val endDateTime = referenceDateTime.toLocalDate().atTime(referenceDateTime.hour, 0)
        val searchWordRankingList = searchHistoryRepository.associateBySearchWord(
            begin = referenceDateTime.minusDays(1L),
            end = endDateTime,
            limit = 20,
        )
        return searchSummaryRepository.saveAll(
            searchWordRankingList.mapIndexed { index, it ->
                SearchSummary(
                    searchWord = it.searchWord,
                    referenceDateTime = endDateTime,
                    searchCount = it.searchCount,
                    searchRank = index + 1,
                )
            }
        ).map { SearchSummaryVo.from(it) }
    }

    override fun getSearchSummaries(requestDateTime: LocalDateTime): List<SearchSummaryVo> {
        // TODO: referenceDateTime 은 config 에서 조회해야함
        return searchSummaryRepository.findByReferenceDateTimeOrderBySearchRankDesc(
            referenceDateTime = requestDateTime.toLocalDate().atTime(requestDateTime.hour, 0)
        ).map { SearchSummaryVo.from(it) }
    }
}