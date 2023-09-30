package com.haeseong.nplusone.ui.search

import com.haeseong.nplusone.domain.search.history.SearchHistoryService
import com.haeseong.nplusone.ui.ApiResponse
import com.haeseong.nplusone.ui.resolveMemberId
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/api/v1/search-histories")
class SearchHistoryController(
    private val searchHistoryService: SearchHistoryService,
) {
    @GetMapping
    fun getSearchHistories(
        @ApiIgnore authentication: Authentication,
    ): ApiResponse<List<SearchHistoryResponse>> {
        val searchHistories = searchHistoryService.getSearchHistories(
            memberId = authentication.resolveMemberId(),
        )
        return ApiResponse.success(
            data = searchHistories.map {
                SearchHistoryResponse(
                    searchHistoryId = it.searchHistoryId,
                    searchWord = it.searchWord,
                )
            },
        )
    }

    @DeleteMapping("/{searchHistoryId}")
    fun delete(
        @ApiIgnore authentication: Authentication,
        @PathVariable searchHistoryId: Long,
    ): ApiResponse<*> {
        searchHistoryService.delete(
            memberId = authentication.resolveMemberId(),
            searchHistoryId = searchHistoryId,
        )
        return ApiResponse.success()
    }

    @DeleteMapping
    fun deleteAll(
        @ApiIgnore authentication: Authentication,
    ): ApiResponse<*> {
        searchHistoryService.deleteAll(
            memberId = authentication.resolveMemberId(),
        )
        return ApiResponse.success()
    }
}
