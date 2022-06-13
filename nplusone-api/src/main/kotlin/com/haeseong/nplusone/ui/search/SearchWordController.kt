package com.haeseong.nplusone.ui.search

import com.haeseong.nplusone.domain.search.summary.SearchSummaryService
import com.haeseong.nplusone.ui.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/search-words")
class SearchWordController(
    private val searchSummaryService: SearchSummaryService,
) {
    @GetMapping
    fun getSearchWords() = ApiResponse.success(
        data = searchSummaryService.getSearchSummaries(LocalDateTime.now()).map {
            // TODO: 인기 검색어 응답에 기준 시각도 추가
            SearchWordResponse(
                searchWord = it.searchWord,
            )
        }
    )
}